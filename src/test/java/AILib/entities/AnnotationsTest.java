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
            entity.react();

            agent.learningIteration(
                    entity.health == h ? -1 : 1,
                    new double[]{entity.health, entity.attack},
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

    @Action(name = "attack")
    public void attackNext(){
        System.out.println("attack");
        this.health-= this.attack;
    }

    @Action(name = "dodge", threshold = 0.6d)
    public void dodge(){
        System.out.println("dodge");
    }
}
