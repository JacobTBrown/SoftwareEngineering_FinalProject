package model.emotions;

////////////////////////////////////////////////////////////////////////
//GAMYGDALA EMOTION ENIGINE CODE. This is STANDALONE AND NOT DEPENDENT ON PHASER!
////////////////////////////////////////////////////////////////////////

import java.awt.geom.Line2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is the main appraisal engine class taking care of interpreting a situation emotionally. Typically you create one instance of this class and then register all agents (emotional entities) to it, as well as all goals.
 *
 * @class TUDelft.Gamygdala
 * @constructor
 */
public class Gamygdala {
    public List<EmotionAgent> agents;
    public List<Goal> goals;
    public Method decayFunction;
    public Method linearDecay;
    public Method exponentialDecay;

    public long lastMillis;
    public long millisPassed;
    public double decayFactor;

    public boolean debug;

    public Gamygdala() {
        try {
            this.linearDecay = Gamygdala.class.getMethod("linearDecay", double.class);
            this.exponentialDecay = Gamygdala.class.getMethod("exponentialDecay", double.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.agents = new ArrayList<>();
        this.goals = new ArrayList<>();
        this.decayFunction = this.exponentialDecay;
        this.decayFunction.setAccessible(true);
        this.decayFactor = 0.8;
        this.lastMillis = System.currentTimeMillis();
        this.millisPassed = 0;
        this.debug = false;
    }

    /**
     * A facilitator method that creates a new Agent and registers it for you
     *
     * @method TUDelft.Gamygdala.createAgent
     * @param {String} agentName The agent with agentName is created
     * @return {TUDelft.Gamygdala.Agent} An agent reference to the newly created agent
     */
    public EmotionAgent createAgent(String agentName) {
        var temp = new EmotionAgent(agentName, this);
        this.registerAgent(temp);
        return temp;
    }

    /**
     * A facilitator method to create a goal for a particular agent, that also registers the goal to the agent and gamygdala.
     * This method is thus handy if you want to keep all gamygdala logic internal to Gamygdala.
     * However, if you want to do more sophisticated stuff (e.g., goals for multiple agents, keep track of your own list of goals to also remove them, appraise events per agent without the need for gamygdala to keep track of goals, etc...) this method will probably be doing too much.
     * @method TUDelft.Gamygdala.createGoalForAgent
     * @param {String} agentName The agent's name to which the newly created goal has to be added.
     * @param {String} goalName The goal's name.
     * @param {double} goalUtility The goal's utility.
     * @param {boolean} isMaintenanceGoal Defines if the goal is a maintenance goal or not [optional]. The default is that the goal is an achievement goal, i.e., a goal that once it's likelihood reaches true (1) or false (-1) stays that way.
     * @return {TUDelft.Gamygdala.Goal} - a goal reference to the newly created goal.
     */
    public Goal createGoalForAgent(String agentName, String goalName, double goalUtility, boolean isMaintenanceGoal){
        EmotionAgent tempAgent = this.getAgentByName(agentName);
        if (tempAgent.name != null) {
            Goal tempGoal = this.getGoalByName(goalName);
            if (tempAgent.goals.contains(tempGoal))
                System.console().printf("Warning: I cannot make a new goal with the same name "+goalName+" as one is registered already. I assume the goal is a common goal and will add the already known goal with that name to the agent "+agentName);
            else {
                tempGoal = new Goal(goalName, goalUtility, isMaintenanceGoal);
                this.registerGoal(tempGoal);
            }
            tempAgent.addGoal(tempGoal);
            return tempGoal;
        } else {
            System.console().printf("Error: agent with name "+ agentName + " does not exist, so I cannot create a goal for it.");
            return null;
        }

    }

    /**
     * A facilitator method to create a relation between two agents. Both source and target have to exist and be registered with this Gamygdala instance.
     * This method is thus handy if you want to keep all gamygdala logic internal to Gamygdala.
     * @method TUDelft.Gamygdala.createRelation
     * @param {String} sourceName The agent who has the relation (the source)
     * @param {String} targetName The agent who is the target of the relation (the target)
     * @param {double} relation The relation (between -1 and 1).
     */
    public void createRelation(String sourceName, String targetName, double relation) {
        EmotionAgent source = this.getAgentByName(sourceName);
        EmotionAgent target = this.getAgentByName(targetName);
        if (source != null && target != null && relation>=-1 && relation<=1){
            source.updateRelation(targetName, relation);
        } else
            System.console().printf("Error: cannot relate " + source + "  to " + target + " with intensity " + relation);
    }

    /**
     * A facilitator method to appraise an event. It takes in the same as what the new Belief(...) takes in, creates a belief and appraises it for all agents that are registered.
     * This method is thus handy if you want to keep all gamygdala logic internal to Gamygdala.
     * @method TUDelft.Gamygdala.appraiseBelief
     * @param {double} likelihood The likelihood of this belief to be true.
     * @param {String} causalAgentName The agent's name of the causal agent of this belief.
     * @param {String[]} affectedGoalNames An array of affected goals' names.
     * @param {double[]} goalCongruences An array of the affected goals' congruences (i.e., the extend to which this event is good or bad for a goal [-1,1]).
     * @param {boolean} [isIncremental] Incremental evidence enforces gamygdala to see this event as incremental evidence for (or against) the list of goals provided, i.e, it will add or subtract this belief's likelihood*congruence from the goal likelihood instead of using the belief as "state" defining the absolute likelihood
     */
    public void appraiseBelief(double likelihood, String causalAgentName, String[] affectedGoalNames, double[] goalCongruences, boolean isIncremental) {
        Belief tempBelief = new Belief(likelihood, causalAgentName, affectedGoalNames, goalCongruences, isIncremental);
        this.appraise(tempBelief, null);
    }

    /**
     * Facilitator method to print all emotional states to the console.
     * @method TUDelft.Gamygdala.printAllEmotions
     * @param {boolean} gain Whether you want to print the gained (true) emotional states or non-gained (false).
     */
    public void printAllEmotions(boolean gain) {
        for (var i = 0; i < this.agents.size(); i++){
            this.agents.get(i).printEmotionalState(gain);
            this.agents.get(i).printRelations(null);
        }
    }

    /**
     * Facilitator to set the gain for the whole set of agents known to TUDelft.Gamygdala.
     * For more realistic, complex games, you would typically set the gain for each agent type separately, to finetune the intensity of the response.
     * @method TUDelft.Gamygdala.setGain
     * @param {double} gain The gain value [0 and 20].
     */
    public void setGain(double gain) {
        for (var i = 0; i < this.agents.size(); i++){
            this.agents.get(i).setGain(gain);
        }
    }

    /**
     * Sets the decay factor and function for emotional decay.
     * It sets the decay factor and type for emotional decay, so that an emotion will slowly get lower in intensity.
     * Whenever decayAll is called, all emotions for all agents are decayed according to the factor and function set here.
     * @method TUDelft.Gamygdala.setDecay
     * @param {double} decayFactor The decayfactor used. A factor of 1 means no decay, a factor
     * @param {function} decayFunction The decay function tobe used. choose between linearDecay or exponentialDecay (see the corresponding methods)
     */
    public void setDecay(double decayFactor, Method decayFunction){
        this.decayFunction = decayFunction;
        this.decayFactor = decayFactor;
    }

    ////////////////////////////////////////////////////////
    //Below this is more detailed gamygdala stuff to use it more flexibly.
    ////////////////////////////////////////////////////////

    /**
     * For every entity in your game (usually NPC's, but can be the player character too) you have to first create an Agent object and then register it using this method.
     * Registering the agent makes sure that Gamygdala will be able to emotionally interpret incoming Beliefs about the game state for that agent.
     * @method TUDelft.Gamygdala.registerAgent
     * @param {TUDelft.Gamygdala.Agent} agent The agent to be registered
     */
    public void registerAgent(EmotionAgent agent) {
        this.agents.add(agent);
        agent.gamygdalaInstance = this;
    }

    /**
     * Simple agent getter by name.
     * @return {TUDelft.Gamygdala.Agent} null or an agent reference that has the name property equal to the agentName argument
     * @param agentName
     */
    public EmotionAgent getAgentByName(String agentName) {
        for(var i = 0; i <this.agents.size(); i++){
            if(this.agents.get(i).name == agentName){
                return this.agents.get(i);
            }
        }
        System.console().printf("Warning: agent " + agentName + " not found");
        return null;
    }


    /**
     * For every goal that NPC's or player characters can have you have to first create a Goal object and then register it using this method.
     * Registering the goals makes sure that Gamygdala will be able to find the correct goal references when a Beliefs about the game state comes in.
     * @method TUDelft.Gamygdala.registerGoal
     * @param {TUDelft.Gamygdala.Goal} goal The goal to be registered.
     */
    public void registerGoal(Goal goal) {
        if (this.getGoalByName(goal.name) == null)
            this.goals.add(goal);
        else{
            System.console().printf("Warning: failed adding a second goal with the same name: " + goal.name);
        }
    }

    /**
     * Simple goal getter by name.
     * @method TUDelft.Gamygdala.getGoalByName
     * @param {String} goalName The name of the goal to be found.
     * @return {TUDelft.Gamygdala.Goal} null or a goal reference that has the name property equal to the goalName argument
     */
    public Goal getGoalByName(String goalName){
        for(var i = 0; i < this.goals.size(); i++){
            if(this.goals.get(i).name == goalName){
                return this.goals.get(i);
            }
        }
        return null;
    }

    /**
     * This method is the main emotional interpretation logic entry point. It performs the complete appraisal of a single event (belief) for all agents (affectedAgent=null) or for only one agent (affectedAgent=true)
     * if affectedAgent is set, then the complete appraisal logic is executed including the effect on relations (possibly influencing the emotional state of other agents),
     * but only if the affected agent (the one owning the goal) == affectedAgent
     * this is sometimes needed for efficiency, if you as a game developer know that particular agents can never appraise an event, then you can force Gamygdala to only look at a subset of agents.
     * Gamygdala assumes that the affectedAgent is indeed the only goal owner affected, that the belief is well-formed, and will not perform any checks, nor use Gamygdala's list of known goals to find other agents that share this goal (!!!)
     * @method TUDelft.Gamygdala.appraise
     * @param {TUDelft.Gamygdala.Belief} belief The current event, in the form of a Belief object, to be appraised
     * @param {TUDelft.Gamygdala.Agent} [affectedAgent] The reference to the agent who needs to appraise the event. If given, this is the appraisal perspective (see explanation above).
     */
    public void appraise(Belief belief, EmotionAgent affectedAgent){
        if (affectedAgent == null) {
            //check all
            if (this.debug)
                System.console().printf(belief.causalAgentName);

            if (belief.goalCongruences.size() != belief.affectedGoalNames.size()){
                System.console().printf("Error: the congruence list was not of the same length as the affected goal list");
                return; //The congruence list must be of the same length as the affected goals list.
            }
            if (this.goals.size() == 0){
                System.console().printf("Warning: no goals registered to Gamygdala, all goals to be considered in appraisal need to be registered.");
                return; //No goals registered to GAMYGDALA.
            }


            for (var i = 0; i < belief.affectedGoalNames.size(); i++) {
                //Loop through every goal in the list of affected goals by this event.
                var currentGoal = this.getGoalByName(belief.affectedGoalNames.get(i));

                if (currentGoal != null){
                    //the goal exists, appraise it
                    var utility = currentGoal.utility;
                    var deltaLikelihood = this.calculateDeltaLikelihood(currentGoal, belief.goalCongruences.get(i), belief.likelihood, belief.isIncremental);
                    //var desirability = belief.goalCongruences[i] * utility;
                    var desirability = deltaLikelihood * utility;
                    if (this.debug)
                        System.console().printf("Evaluated goal: " + currentGoal.name + "(" + utility + ", " + deltaLikelihood + ")");

                    //now find the owners, and update their emotional states
                    for (var j = 0; j < this.agents.size(); j++){
                        if (this.agents.get(j).hasGoal(currentGoal.name)){
                            var owner = this.agents.get(j);

                            if (this.debug)
                                System.console().printf("....owned by " + owner.name);
                            this.evaluateInternalEmotion(utility, deltaLikelihood, currentGoal.likelihood, owner);
                            this.agentActions(owner.name, belief.causalAgentName, owner.name, desirability, utility, deltaLikelihood);
                            //now check if anyone has a relation to this goal owner, and update the social emotions accordingly.
                            for (var k = 0; k < this.agents.size(); k++){
                                var relation = this.agents.get(k).getRelation(owner.name);
                                if (relation != null){
                                    if (this.debug){
                                        System.console().printf(this.agents.get(k).name + " has a relationship with " + owner.name);
                                        System.console().printf(relation.agentName);
                                    }
                                    //The agent has relationship with the goal owner which has nonzero utility, add relational effects to the relations for agent[k].
                                    this.evaluateSocialEmotion(utility, desirability, deltaLikelihood, relation, this.agents.get(k));
                                    //also add remorse and gratification if conditions are met within (i.e., agent[k] did something bad/good for owner)
                                    this.agentActions(owner.name, belief.causalAgentName, this.agents.get(k).name, desirability, utility, deltaLikelihood);
                                } else {
                                    if (this.debug)
                                        System.console().printf(this.agents.get(k).name + " has NO relationship with " + owner.name);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            //check only affectedAgent (which can be much faster) and does not involve console output nor checks
            for (var i = 0; i < belief.affectedGoalNames.size(); i++) {
                //Loop through every goal in the list of affected goals by this event.
                var currentGoal = affectedAgent.getGoalByName(belief.affectedGoalNames.get(i));
                var utility = currentGoal.utility;
                var deltaLikelihood = this.calculateDeltaLikelihood(currentGoal, belief.goalCongruences.get(i), belief.likelihood, belief.isIncremental);
                //var desirability = belief.goalCongruences[i] * utility;
                var desirability = deltaLikelihood * utility;
                //assume affectedAgent is the only owner to be considered in this appraisal round.

                var owner = affectedAgent;

                this.evaluateInternalEmotion(utility, deltaLikelihood, currentGoal.likelihood, owner);
                this.agentActions(owner.name, belief.causalAgentName, owner.name, desirability, utility, deltaLikelihood);
                //now check if anyone has a relation to this goal owner, and update the social emotions accordingly.
                for (var k = 0; k < this.agents.size(); k++){
                    var relation = this.agents.get(k).getRelation(owner.name);
                    if(relation != null){
                        if (this.debug){
                            System.console().printf(this.agents.get(k).name + " has a relationship with " + owner.name);
                            System.console().printf(relation.agentName);
                        }
                        //The agent has relationship with the goal owner which has nonzero utility, add relational effects to the relations for agent[k].
                        this.evaluateSocialEmotion(utility, desirability, deltaLikelihood, relation, this.agents.get(k));
                        //also add remorse and gratification if conditions are met within (i.e., agent[k] did something bad/good for owner)
                        this.agentActions(owner.name, belief.causalAgentName, this.agents.get(k).name, desirability, utility, deltaLikelihood);
                    } else {
                        if (this.debug)
                            System.console().printf(this.agents.get(k).name + " has NO relationship with " + owner.name);
                    }
                }
            }
        }
        //print the emotions to the console for debugging
        if (this.debug){
            this.printAllEmotions(false);
            //this.printAllEmotions(true);
        }
    }

    /**
     * This method decays for all registered agents the emotional state and relations. It performs the decay according to the time passed, so longer intervals between consecutive calls result in bigger clunky steps.
     * Typically this is called automatically when you use startDecay(), but you can use it yourself if you want to manage the timing.
     * This function is keeping track of the millis passed since the last call, and will (try to) keep the decay close to the desired decay factor, regardless the time passed
     * So you can call this any time you want (or, e.g., have the game loop call it, or have e.g., Phaser call it in the plugin update, which is default now).
     * Further, if you want to tweak the emotional intensity decay of individual agents, you should tweak the decayFactor per agent not the "frame rate" of the decay (as this doesn't change the rate).
     * @method TUDelft.Gamygdala.decayAll
     */
    public void decayAll() {
        this.millisPassed = System.currentTimeMillis() - this.lastMillis;
        this.lastMillis= System.currentTimeMillis();
        for (var i = 0; i < this.agents.size(); i++){
            this.agents.get(i).decay(this);
        }
    }

    ////////////////////////////////////////////////////////
    //Below this is internal gamygdala stuff not to be used publicly (i.e., never call these methods).
    ////////////////////////////////////////////////////////

    private double calculateDeltaLikelihood(Goal goal, double congruence, double likelihood, boolean isIncremental){
        //Defines the change in a goal's likelihood due to the congruence and likelihood of a current event.
        //We cope with two types of beliefs: incremental and absolute beliefs. Incrementals have their likelihood added to the goal, absolute define the current likelihood of the goal
        //And two types of goals: maintenance and achievement. If an achievement goal (the default) is -1 or 1, we can't change it any more (unless externally and explicitly by changing the goal.likelihood).
        double oldLikelihood = goal.likelihood;
        double newLikelihood;
        if (goal.isMaintenanceGoal == false && (oldLikelihood >= 1 | oldLikelihood <= -1))
            return 0;

        if (goal.calculateLikelyhood){
            //if the goal has an associated function to calculate the likelyhood that the goal is true, then use that function,
            newLikelihood = goal.calculateLikelyhood();
        } else {
            //otherwise use the event encoded updates.
            if (isIncremental){
                newLikelihood = oldLikelihood + likelihood * congruence;
                newLikelihood = Math.max(Math.min(newLikelihood, 1), -1);
            }
            else
                newLikelihood = (congruence * likelihood + 1.0) / 2.0;
        }

        goal.likelihood = newLikelihood;

        if(oldLikelihood != 0){         //TODO: Set to Zero (Might have to change)
            return newLikelihood - oldLikelihood;
        } else {
            return newLikelihood;
        }
    }

    private void evaluateInternalEmotion(double utility, double deltaLikelihood, double likelihood, EmotionAgent agent) {
        //This method evaluates the event in terms of internal emotions that do not need relations to exist, such as hope, fear, etc..
        boolean positive = true;
        double intensity;
        List<String> emotion = new ArrayList<>();

        if(utility >= 0){
            if ( deltaLikelihood >= 0){
                positive = true;
            }else {
                positive = false;
            }
        } else if ( utility < 0){
            if( deltaLikelihood >= 0){
                positive = false;
            } else {
                positive = true;
            }
        }
        if(likelihood > 0 && likelihood < 1){
            if (positive == true){
                emotion.add("hope");
            }else {
                emotion.add("fear");
            }
        } else if (likelihood == 1){
            if (utility >= 0){
                if(deltaLikelihood < 0.5){
                    emotion.add("satisfaction");
                }
                emotion.add("joy");
            }
            else {
                if (deltaLikelihood < 0.5){
                    emotion.add("fear-confirmed");
                }
                emotion.add("distress");
            }
        } else if (likelihood == 0){
            if (utility >= 0){
                if (deltaLikelihood > 0.5){
                    emotion.add("disappointment");
                }
                emotion.add("distress");
            } else {
                if (deltaLikelihood > 0.5){
                    emotion.add("relief");
                }
                emotion.add("joy");
            }
        }
        intensity = Math.abs(utility * deltaLikelihood);
        if (intensity != 0){
            for(var i = 0; i < emotion.size(); i++){
                agent.updateEmotionalState(new Emotion(emotion.get(i), intensity));
            }
        }
    }

    private void evaluateSocialEmotion(double utility, double desirability, double deltaLikelihood, Relation relation, EmotionAgent agent) {
        //This function is used to evaluate happy-for, pity, gloating or resentment.
        //Emotions that arise when we evaluate events that affect goals of others.
        //The desirability is the desirability from the goal owner's perspective.
        //The agent is the agent getting evaluated (the agent that gets the social emotion added to his emotional state).
        //The relation is a relation object between the agent being evaluated and the goal owner of the affected goal.
        Emotion emotion = new Emotion(null,0);

        if (desirability >= 0){
            if(relation.like >= 0){
                emotion.name = "happy-for";
            }
            else {
                emotion.name = "resentment";
            }
        }
        else {
            if (relation.like >= 0){
                emotion.name = "pity";
            }
            else {
                emotion.name = "gloating";
            }
        }
        emotion.intensity = Math.abs(utility * deltaLikelihood * relation.like);
        if(emotion.intensity != 0){
            relation.addEmotion(emotion);
            agent.updateEmotionalState(emotion);  //also add relation emotion the emotion to the emotional state
        }
    }

    private void agentActions(String affectedName, String causalName, String selfName, double desirability, double utility, double deltaLikelihood) {
        if (causalName != null && causalName != ""){
            //If the causal agent is null or empty, then we we assume the event was not caused by an agent.
            //There are three cases here.
            //The affected agent is SELF and causal agent is other.
            //The affected agent is SELF and causal agent is SELF.
            //The affected agent is OTHER and causal agent is SELF.
            var emotion = new Emotion(null,0);
            Relation relation;
            if(affectedName == selfName && selfName != causalName){
                //Case one
                if (desirability >= 0)
                    emotion.name = "gratitude";
                else
                    emotion.name = "anger";

                emotion.intensity = Math.abs(utility * deltaLikelihood);
                EmotionAgent self = this.getAgentByName(selfName);

                if (self.hasRelationWith(causalName))
                    relation = self.getRelation(causalName);
                else {
                    self.updateRelation(causalName, 0.0);
                    relation = self.getRelation(causalName);
                }

                relation.addEmotion(emotion);
                self.updateEmotionalState(emotion);  //also add relation emotion the emotion to the emotional state
            }
            if(affectedName == selfName && selfName == causalName){
                //Case two
                //This case is not included in TUDelft.Gamygdala.
                //This should include pride and shame
            }
            if(affectedName != selfName && causalName == selfName){
                //Case three
                if (this.getAgentByName(causalName).hasRelationWith(affectedName)) {
                    relation = this.getAgentByName(causalName).getRelation(affectedName);
                    if (desirability >= 0) {
                        if (relation.like >= 0) {
                            emotion.name = "gratification";
                            emotion.intensity = Math.abs(utility * deltaLikelihood * relation.like);
                            relation.addEmotion(emotion);
                            this.getAgentByName(causalName).updateEmotionalState(emotion);  //also add relation emotion the emotion to the emotional state
                        }
                    }
                    else {
                        if (relation.like >= 0){
                            emotion.name = "remorse";
                            emotion.intensity = Math.abs(utility * deltaLikelihood * relation.like);
                            relation.addEmotion(emotion);
                            this.getAgentByName(causalName).updateEmotionalState(emotion);  //also add relation emotion the emotion to the emotional state
                        }

                    }

                }
            }
        }
    }

    /** A linear decay function that will decrease the emotion intensity of an emotion every tick by a constant defined by the decayFactor in the gamygdala instance.
     * You can set Gamygdala to use this function for all emotion decay by calling setDecay() and passing this function as second parameter. This function is not to be called directly.
     * @method TUDelft.Gamygdala.linearDecay
     */
    private double linearDecay(double value) {
        //assumes the decay of the emotional state intensity is linear with a factor equal to decayFactor per second.
        return value - this.decayFactor * (this.millisPassed / 1000);
    }

    /** An exponential decay function that will decrease the emotion intensity of an emotion every tick by a factor defined by the decayFactor in the gamygdala instance.
     * You can set Gamygdala to use this function for all emotion decay by calling setDecay() and passing this function as second parameter. This function is not to be called directly.
     * @method TUDelft.Gamygdala.exponentialDecay
     */
    private double exponentialDecay(double value) {
        //assumes the decay of the emotional state intensity is exponential with a factor equal to decayFactor per second.
        return value * Math.pow(this.decayFactor, this.millisPassed / 1000);
    }

}