package ai.engine.kepoj.utils;

import ai.engine.kepoj.anotations.Action;
import ai.engine.kepoj.anotations.ActionsList;
import ai.engine.kepoj.anotations.Parameter;
import ai.engine.kepoj.exceptions.EntityParseException;
import ai.engine.kepoj.functions.StandardFunctions;
import ai.engine.kepoj.agents.Agent;
import ai.engine.kepoj.agents.deep.QNeuralNetwork;
import ai.engine.kepoj.layers.StaticLayer;
import org.junit.jupiter.api.Test;

public class AnnotationsTest {
    @Test
    public void main() throws Exception {
        QNeuralNetwork agent = new QNeuralNetwork(2);
        agent.addLayer(new StaticLayer(4, StandardFunctions.SIGMOID));
        agent.addLayer(new StaticLayer(4, StandardFunctions.SIGMOID));

        Entity entity = new Entity(120, 12, agent);

        while (entity.health > 0) {
            int h = entity.health;
            double reward = 0;
            switch (entity.react()){
                case EntityController.INACTION: reward = 0.5; break;
                case "attack": reward = 1; break;
                case "dodge": reward = -1; break;
                case "left": reward = 0.5d; break;
                case "right": reward = -0.5d; break;
                default:
                    System.out.println("Oops");
            }

            agent.learningIteration(
                    reward,
                    entity.getParametersState(),
                    0.99, 1
                    );
        }
    }
}

class Entity extends EntityController {
    public @Parameter int health;
    public final @Parameter int attack;

    public Entity(int health, int attack, Agent agent) {
        super(agent);
        this.health = health;
        this.attack = attack;

        try {
            super.bind(this);
        } catch (EntityParseException e) {
            e.printStackTrace();
        }
    }

    @Action(name = "attack")
    public void attackNext(){
        System.out.println("attack");
        this.health-= this.attack;
    }

    @Action(name = "dodge")
    public void dodge(){
        System.out.println("dodge");
    }

    @ActionsList(names = {"left", "right"})
    public void move(String action){
        System.out.println(action);
        this.health -= 0.1;
    }
}
