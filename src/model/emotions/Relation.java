package model.emotions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Relation {
    public String agentName;
    public double like;
    public List<Emotion> emotionList;

    /**
     * This is the class that represents a relation one agent has with other agents.
     * It's main role is to store and manage the emotions felt for a target agent (e.g angry at, or pity for).
     * Each agent maintains a list of relations, one relation for each target agent.
     * @class TUDelft.Gamygdala.Relation
     * @constructor
     * @param {String} targetName The agent who is the target of the relation.
     * @param {double} relation The relation [-1 and 1].
     */
    public Relation(String targetName, double like) {
        this.agentName = targetName ;
        this.like = like;
        this.emotionList = new ArrayList<>();
    }

    public void addEmotion(Emotion emotion) {
        var added = false;
        for (var i = 0; i < this.emotionList.size(); i++) {
            if (this.emotionList.get(i).name == emotion.name) {
			/*
            if (this.emotionList[i].intensity < emotion.intensity){
                this.emotionList[i].intensity = emotion.intensity;
            }*/
                this.emotionList.get(i).intensity += emotion.intensity;
                added = true;
            }
        }
        if(added == false){
            //copy on keep, we need to maintain a list of current emotions for the relation, not a list refs to the appraisal engine
            this.emotionList.add(new Emotion(emotion.name, emotion.intensity));
        }
    };

    public void decay(Gamygdala gamygdalaInstance){
        for (var i = 0; i < this.emotionList.size(); i++){
            double newIntensity = 0;

            try {
                newIntensity = (double) gamygdalaInstance.decayFunction.invoke(this.emotionList.get(i).intensity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            if (newIntensity < 0){
                //This emotion has decayed below zero, we need to remove it.
                this.emotionList.remove(i);
            }
            else
                this.emotionList.get(i).intensity = newIntensity;
        }
    };
}
