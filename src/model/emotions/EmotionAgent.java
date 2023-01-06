package model.emotions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is the emotion agent class taking care of emotion management for one entity
 *
 * @class TUDelft.Gamygdala.Agent
 * @constructor
 * @param {String} name The name of the agent to be created. This name is used as ref throughout the appraisal engine.
 */
public class EmotionAgent {
    public String name;
    public List<Goal> goals;
    public List<Relation> currentRelations;
    public List<Emotion> internalState;
    public Map<String, double[]> mapPAD;

    public double gain = 1.0;
    public Gamygdala gamygdalaInstance;

    public EmotionAgent(String name, Gamygdala gamygdalaInstance) {
        this.name = name;
        this.goals              = new ArrayList<>();
        this.currentRelations   = new ArrayList<>();
        this.internalState      = new ArrayList<>();
        this.gamygdalaInstance  = gamygdalaInstance;

        this.mapPAD.put("distress",         new double[]{-0.61, 0.28,-0.36});
        this.mapPAD.put("fear",             new double[]{-0.64, 0.60,-0.43});
        this.mapPAD.put("hope",             new double[]{ 0.51, 0.23, 0.14});
        this.mapPAD.put("joy",              new double[]{ 0.76, 0.48, 0.35});
        this.mapPAD.put("satisfaction",     new double[]{ 0.87, 0.20, 0.62});
        this.mapPAD.put("fear-confirmed",   new double[]{-0.61, 0.06,-0.32});   //defeated
        this.mapPAD.put("disappointment",   new double[]{-0.61,-0.15,-0.29});
        this.mapPAD.put("relief",           new double[]{ 0.29,-0.19,-0.28});
        this.mapPAD.put("happy-for",        new double[]{ 0.64, 0.35, 0.25});
        this.mapPAD.put("resentment",       new double[]{-0.35, 0.35, 0.29});
        this.mapPAD.put("pity",             new double[]{-0.52, 0.02,-0.21});   //regretful
        this.mapPAD.put("gloating",         new double[]{-0.45, 0.48, 0.42});   //cruel
        this.mapPAD.put("gratitude",        new double[]{ 0.64, 0.16,-0.21});   //grateful
        this.mapPAD.put("anger",            new double[]{-0.51, 0.59, 0.25});
        this.mapPAD.put("gratification",    new double[]{ 0.69, 0.57, 0.63});   //triumphant
        this.mapPAD.put("remorse",          new double[]{-0.57, 0.28,-0.34});   //guilty
    }

    /**
     * Adds a goal to this agent's goal list (so this agent becomes an owner of the goal)
     * @method TUDelft.Gamygdala.Agent.addGoal
     * @param {TUDelft.Gamygdala.Goal} goal The goal to be added.
     */
    public void addGoal(Goal goal) {
        //no copy, cause we need to keep the ref,
        //one goal can be shared between agents so that changes to this one goal are reflected in the emotions of all agents sharing the same goal
        this.goals.add(goal);
    }

    /**
     * Adds a goal to this agent's goal list (so this agent becomes an owner of the goal)
     * @method TUDelft.Gamygdala.Agent.removeGoal
     * @param {String} goalName The name of the goal to be added.
     * @return {boolean} True if the goal could be removed, false otherwise.
     */
    public boolean removeGoal(String goalName){
        for (var i = 0; i < this.goals.size(); i++){
            if (this.goals.get(i).name == goalName){
                this.goals.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if this agent owns a goal.
     * @param {String} goalName The name of the goal to be checked.
     * @return {boolean} True if this agent owns the goal, false otherwise.
     */
    public boolean hasGoal(String goalName){
        return (this.getGoalByName(goalName) != null);
    }

    /**
     * If this agent has a goal with name goalName, this method returns that goal.
     * @method TUDelft.Gamygdala.Agent.getGoalByName
     * @param {String} goalName The name of the goal to be found.
     * @return {TUDelft.Gamygdala.Goal} the reference to the goal.
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
     * Sets the gain for this agent.
     * @method TUDelft.Gamygdala.Agent.setGain
     * @param {double} gain The gain value [0 and 20].
     */
    public void setGain(double gain) {
        if (gain <= 0 || gain > 20)
            System.console().printf("Error: gain factor for appraisal integration must be between 0 and 20");
        else
            this.gain = gain;
    }

    /**
     * A facilitating method to be able to appraise one event only from the perspective of the current agent (this).
     * Needs an instantiated gamygdala object (automatic when the agent is registered with Gamygdala.registerAgent(agent) to a Gamygdala instance).
     * @method TUDelft.Gamygdala.Agent.appraise
     * @param {TUDelft.Gamygdala.Belief} belief The belief to be appraised.
     */
    public void appraise(Belief belief) {
        this.gamygdalaInstance.appraise(belief, this);
    }

    public void updateEmotionalState(Emotion emotion) {
        for (var i = 0; i < this.internalState.size(); i++){
            if (this.internalState.get(i).name == emotion.name){
                //Appraisals simply add to the old value of the emotion
                //So repeated appraisals without decay will result in the sum of the appraisals over time
                //To decay the emotional state, call .decay(decayFunction), or simply use the facilitating function in Gamygdala setDecay(timeMS).
                this.internalState.get(i).intensity += emotion.intensity;
                return;
            }
        }
        //copy on keep, we need to maintain a list of current emotions for the state, not a list references to the appraisal engine
        this.internalState.add(new Emotion(emotion.name, emotion.intensity));
    }

    /**
     * This function returns either the state as is (gain=false) or a state based on gained limiter (limited between 0 and 1), of which the gain can be set by using setGain(gain).
     * A high gain factor works well when appraisals are small and rare, and you want to see the effect of these appraisals
     * A low gain factor (close to 0 but in any case below 1) works well for high frequency and/or large appraisals, so that the effect of these is dampened.
     * @method TUDelft.Gamygdala.Agent.getEmotionalState
     * @param {boolean} useGain Whether to use the gain function or not.
     * @return {TUDelft.Gamygdala.Emotion[]} An array of emotions.
     */
    public List<Emotion> getEmotionalState(boolean useGain) {
        if (useGain){
            List<Emotion> gainState = new ArrayList<Emotion>();

            for (var i = 0; i < this.internalState.size(); i++){
                var gainEmo = (this.gain * this.internalState.get(i).intensity) / (this.gain * this.internalState.get(i).intensity + 1.0);
                gainState.add(new Emotion(this.internalState.get(i).name, gainEmo));
            }

            return gainState;
        } else
            return this.internalState;
    }

    /**
     * This function returns a summation-based Pleasure Arousal Dominance mapping of the emotional state as is (gain=false), or a PAD mapping based on a gained limiter (limited between 0 and 1), of which the gain can be set by using setGain(gain).
     * It sums over all emotions the equivalent PAD values of each emotion (i.e., [P,A,D]=SUM(Emotion_i([P,A,D])))), which is then gained or not.
     * A high gain factor works well when appraisals are small and rare, and you want to see the effect of these appraisals.
     * A low gain factor (close to 0 but in any case below 1) works well for high frequency and/or large appraisals, so that the effect of these is dampened.
     * @method TUDelft.Gamygdala.Agent.getPADState
     * @param {boolean} useGain Whether to use the gain function or not.
     * @return {double[]} An array of doubles with Pleasure at index 0, Arousal at index [1] and Dominance at index [2].
     */
    public double[] getPADState(boolean useGain) {
        var PAD = new double[]{0, 0, 0};

        for (var i = 0; i < this.internalState.size(); i++) {
            PAD[0] += this.internalState.get(i).intensity * this.mapPAD.get(this.internalState.get(i).name)[0];
            PAD[1] += this.internalState.get(i).intensity * this.mapPAD.get(this.internalState.get(i).name)[1];
            PAD[2] += this.internalState.get(i).intensity * this.mapPAD.get(this.internalState.get(i).name)[2];
        }
        if (useGain){
            PAD[0] = (PAD[0] >= 0 ? this.gain*PAD[0] / (this.gain*PAD[0]+1) : -this.gain*PAD[0]/(this.gain*PAD[0]-1));
            PAD[1] = (PAD[1] >= 0 ? this.gain*PAD[1] / (this.gain*PAD[1]+1) : -this.gain*PAD[1]/(this.gain*PAD[1]-1));
            PAD[2] = (PAD[2] >= 0 ? this.gain*PAD[2] / (this.gain*PAD[2]+1) : -this.gain*PAD[2]/(this.gain*PAD[2]-1));
            return PAD;
        } else
            return PAD;
    }

    /**
     * This function prints to the console either the state as is (gain=false) or a state based on gained limiter (limited between 0 and 1), of which the gain can be set by using setGain(gain).
     * A high gain factor works well when appraisals are small and rare, and you want to see the effect of these appraisals
     * A low gain factor (close to 0 but in any case below 1) works well for high frequency and/or large appraisals, so that the effect of these is dampened.
     * @method TUDelft.Gamygdala.Agent.printEmotionalState
     * @param {boolean} useGain Whether to use the gain function or not.
     */
    public void printEmotionalState(boolean useGain) {
        var output = this.name + " feels ";
        var i = 0;
        var emotionalState = this.getEmotionalState(useGain);
        for (; i < emotionalState.size(); i++){
            output += emotionalState.get(i).name + " : " + emotionalState.get(i).intensity + ", ";
        }
        if (i > 0)
            System.console().printf(output);
    }

    /**
     * Sets the relation this agent has with the agent defined by agentName. If the relation does not exist, it will be created, otherwise it will be updated.
     * @method TUDelft.Gamygdala.Agent.updateRelation
     * @param {String} agentName The agent who is the target of the relation.
     * @param {double} like The relation (between -1 and 1).
     */
    public void updateRelation(String agentName, double like) {
        if (!this.hasRelationWith(agentName)) {
            //This relation does not exist, just add it.
            this.currentRelations.add(new Relation(agentName, like));
        } else {
            //The relation already exists, update it.
            for (var i = 0; i < this.currentRelations.size(); i++){
                if (this.currentRelations.get(i).agentName == agentName){
                    this.currentRelations.get(i).like = like;
                }
            }
        }
    }

    /**
     * Checks if this agent has a relation with the agent defined by agentName.
     * @method TUDelft.Gamygdala.Agent.hasRelationWith
     * @param {String} agentName The agent who is the target of the relation.
     * @return {boolean} True if the relation exists, otherwise false.
     */
    public boolean hasRelationWith(String agentName){
        return (this.getRelation(agentName) != null);
    }

    /**
     * Returns the relation object this agent has with the agent defined by agentName.
     * @method TUDelft.Gamygdala.Agent.getRelation
     * @param {String} agentName The agent who is the target of the relation.
     */
    public Relation getRelation(String agentName){
        for (var i = 0; i < this.currentRelations.size(); i++){
            if (this.currentRelations.get(i).agentName == agentName){
                return this.currentRelations.get(i);
            }
        }
        return null;
    }

    /**
     * Returns the relation object this agent has with the agent defined by agentName.
     * @method TUDelft.Gamygdala.Agent.printRelations
     * @param {String} [agentName] The agent who is the target of the relation will only be printed, or when omitted all relations are printed.
     */
    public void printRelations(String agentName){
        var output = this.name + " has the following sentiments:\n   ";
        var i = 0;
        var found = false;
        for (; i < this.currentRelations.size(); i++) {
            if (agentName == null || this.currentRelations.get(i).agentName == agentName) {
                for (var j = 0; j < this.currentRelations.get(i).emotionList.size(); j++){
                    output += this.currentRelations.get(i).emotionList.get(j).name + "(" + this.currentRelations.get(i).emotionList.get(j).intensity + ") ";
                    found  = true;
                }
            }
            output += " for " + this.currentRelations.get(i).agentName;
            if (i < this.currentRelations.size() - 1)
                output += ", and\n   ";
        }
        if (found)
            System.console().printf(output);
    }

    /**
     * This method decays the emotional state and relations according to the decay factor and function defined in gamygdala.
     * Typically this is called automatically when you use startDecay() in Gamygdala, but you can use it yourself if you want to manage the timing.
     * This function is keeping track of the millis passed since the last call, and will (try to) keep the decay close to the desired decay factor, regardless the time passed
     * So you can call this any time you want (or, e.g., have the game loop call it, or have e.g., Phaser call it in the plugin update, which is default now).
     * Further, if you want to tweak the emotional intensity decay of individual agents, you should tweak the decayFactor per agent not the "frame rate" of the decay (as this doesn't change the rate).
     * @method TUDelft.Gamygdala.decayAll
     * @param {TUDelft.Gamygdala} gamygdalaInstance A reference to the correct gamygdala instance that contains the decayFunction property to be used )(so you could use different gamygdala instances to manage different groups of  agents)
     */
    public void decay(Gamygdala gamygdalaInstance) {
        for (var i = 0; i < this.internalState.size(); i++){
            double newIntensity = 0;
            try {
                newIntensity = (double) this.gamygdalaInstance.decayFunction.invoke(this.internalState.get(i).intensity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (newIntensity < 0){
                this.internalState.remove(i);
            } else {
                this.internalState.get(i).intensity = newIntensity;
            }
        }
        for (var i = 0; i < this.currentRelations.size(); i++)
            this.currentRelations.get(i).decay(gamygdalaInstance);
    }
}