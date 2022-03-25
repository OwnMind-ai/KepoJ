package AILib.entities;

import AILib.agents.Agent;
import AILib.agents.QAgent;
import AILib.anotations.Action;
import AILib.anotations.Parameter;
import AILib.functions.StandardFunctions;
import AILib.layers.StaticLayer;
import AILib.utills.EntityController;
import org.junit.jupiter.api.Test;

public class AnnotationsTest {
    @Test
    public void main() throws Exception {
        QAgent agent = new QAgent(2);
        agent.addLayer(new StaticLayer(4, StandardFunctions.SIGMOID));
        agent.addLayer(new StaticLayer(2, StandardFunctions.SIGMOID));

        Entity entity = new Entity(120, 12, agent);

        while (entity.health > 0) {
            int h = entity.health;
            double reward = 0;
            switch (entity.react()){
                case EntityController.INACTION: reward = 0.5; break;
                case "attack": reward = 1; break;
                case "dodge": reward = -1; break;
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

        super.bind(this);
    }

    @Action(name = "attack", threshold = 0.8d)
    public void attackNext(){
        System.out.println("attack");
        this.health-= this.attack;
    }

    @Action(name = "dodge", threshold = 0.6d)
    public void dodge(){
        System.out.println("dodge");
    }

    @Action(name = INACTION)
    public void stay(){
        System.out.println("stay");
        this.health-= this.attack/2;
    }
}
