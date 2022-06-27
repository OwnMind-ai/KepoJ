# KepoJ
KepoJ is a library that provides creating, using, training and exporting ml-agents and neural networks.

## Maven dependency
    <dependency>
        <groupId>ai.engine</groupId>
        <artifactId>KepoJ</artifactId>
        <version>1.2</version>
    </dependency>
## Neural Networks
#### Step #1: Initialize NeuralNetwork
The main class that implements a neural network is the `NeuralNetwork` class. It has two subclasses, `SupervisedNeuralNetwork` and `QNeuralNetwork`. Each of them provides neural network training according to different **algorithms**.

    // Creates a NN with a fixed number of input neurons, in that case is 9
    SupervisedNeuralNetwork network = new SupervisedNeuralNetwork(9);
    
    // Loads the NN class from file "nn.bin"
    SupervisedNeuralNetwork loaded = new SupervisedNeuralNetwork("nn.bin");

#### Step #2: Adding layers
There are several implementations of the `Layer` interface: `StaticLayer`, `DynamicLayer`, `ConvolutionLayer`. The most common is the `StaticLayer`, which provides a fixed number of neurons in it.

To add layers, use the `NeuralNetwork::add(Layer layer)` or `NeuralNetwork::addAll(Layer...layers)` methods.

    network.addAll(
        // Static layer with 4 neurons in it
        new StaticLayer(4, StandardFunctions.SIGMOID),   

        new StaticLayer(3, StandardFunctions.SIGMOID),
        new StaticLayer(2, StandardFunctions.SIGMOID)
    );

#### Step #3: Execution
To run the neural network, use the `react(double... data)` method. The `data` argument is a double array of the same size as the **input layer**.

    double result = network.react(1, 1, 1, 0, 0, 0, 0, 0 , 0);
    System.out.println(result);  

#### Step #4.1: Supervised Training
Supervised neural network training is always based on a **Dataset** object that contains a set of input data and correct answers to them.
In order to control the accuracy of training, you need to set the `learning rate` as an argument in the method between 0 and 1.

There are two methods of training that differ in terms of termination of training. If the last argument type is **long**, it will mean that training will continue for a fixed number of cycles (ages).
If the type is **double**, then this will mean the minimum allowable error for the neural network.

    Dataset example = new Dataset(new double[][][]{
        {{1,1,1,0,0,0,0,0,0},{0.8d,0.2d}},
        ...
        {{0,0,0,0,0,0,0,0,0},{0.2d,0.2d}}
    });

    // Specifies that the training result will be printed to the console
    network.isPrinting(true);

    // network is instance of the SupervisedNeuralNetwork class
    network.train(example, 0.5, 0.0005d);

#### Step #4.2: Q Training
QNeuralNetwork uses the learningIteration training method, which is a one-time update of the weights according to the next environment state and reward.

The discount factor (third argument) is controlling value that will take to power by iteration number that increasing after every method invoking. Iteration is a public field, so you can easily change it.

    for (int i = 0; i < 10; i++){
        double[] result = network.react(example.get(i)[0]);
        double reward = Arrays.equals(result, example.get(i)[1]);

        // network is instance of the QNeuralNetwork
        network.learningIteration(reward, example.get(i + 1)[0], 0.9, 0.5);
    }

#### Step #5: Export and Import
KepoJ uses the **Serialization API** to export, which means you can use methods that work with files, or you can serialize NNs your way.
    
    // Exporting to file 
    network.save("filename");

    // Importing from file
    new NeuralNetwork("filename");

## EntityController
`EntityController` is an object which can operate with an instance of a class, called entity class, according to **agent** decision.

#### Step #1: Creating Entity class

The Entity class must contain fields and methods that have these annotations:

- @Parameter - used to specify field (only for double types) which will be used as input data for agent
- @Action - used to specify the methods that will be called if its action is selected
- @ActionList (not required) - used to specify a method that will take an action name as an argument and process it


        // Extending EntityController used to attach agent exactly at entity class 
        // and not required
        class Entity extends EntityController {
            public @Parameter int health;
            public final @Parameter int attack;
        
            public Entity(int health, int attack, Agent agent) {
                super(agent);
                this.health = health;
                this.attack = attack;
        
                try {
                    super.bind(this);
                } catch (EntityParseException e) { /* ... */ }
            }
        
            @Action(name = "attack")
            public void attack(){ this.health-= this.attack; }
        
            @Action(name = "dodge")
            public void dodge(){ }
        
            @ActionsList(names = {"left", "right"})
            public void move(String action){ this.health -= 0.1; }
       }

#### Step #2: Attaching Controller
There are two ways to attach controller to an entity: **internal** and **external**.

#### Internal
    
    class Entity extends EntityContoller{
    /* ...Class Structure... */
        public Entity(Agent agent) throws EntityParseException{
            super(agent);
            super.bind(this)
        }
    /* ...Class Struct... */
    }

#### External

    // Class Entity doesn't extend EntityController
    Entity entity = new Entity();

    EntityController controller = new EntityController(agent, entity);

#### Step #3: Execution
The Entity Controller has several methods to execute that differ in how they get input for the agent. 

**Important**, all methods must match the length of the agent's input.
    
    // Uses only provided data 
    controller.rawReact(0, 1, 2, 3, 4);

    // Uses entity parameters as first and provided data at the end of array
    // Values 0, 1, 2 are taken from entity parameters
    controller.react(3, 4);

    // Uses only entity parameters
    controller.react();

After using these methods, the object will call the action methods and return the name of the action that was performed.
