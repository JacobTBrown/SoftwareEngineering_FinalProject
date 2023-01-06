package model.emotions;

/**
 * This class is mainly a data structure to store an emotion with its intensity
 * @class TUDelft.Gamygdala.Emotion
 * @constructor
 * @param {String} name The string ref of the emotion
 * @param {double} intensity The intensity at which the emotion is set upon construction.
 */
public class Emotion {
    public String name;
    public double intensity;

    public Emotion(String name, double intensity) {
        this.name = name;
        this.intensity = intensity;
    }
}
