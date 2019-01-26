package Game;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;



public class Main {

    private static final int DISPLAY_WIDTH = 700;
    private static final int DISPLAY_HEIGHT = 500;

    private static final int MAP_WIDTH = 1500;
    private static final int MAP_HEIGHT = 900;

    private static final int FRAMES_PER_SECOND = 30;

    static long ID = -1; // we get ID from the server side

    private TcpConnection connections; // establishing TCP connection

    private CharacterObj character; // data about the main character
    private List<Bullet> bullets; // bullets shot in every frame, also to server

    private List<Box> obstacles;
    private List<Box> movingObjects; // all players and bullets. We get this from server
    private Box updatedCharacter; // clients character that we get from server

    private Camera camera;

    private String server_ip;
    private int server_port_tcp;
    private int client_port_udp;
    long window;
    private GLFWErrorCallback errorCallback;
    public GLFWKeyCallback keyCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;


    public static void main(String[] args) {
        Main main = new Main("127.0.0.1",5555,-1);
        main.initOpenGl();

        main.init();
        main.start();
    }

    public Main(String ip, int portTcp, int portUdp){
        server_ip = ip;
        server_port_tcp = portTcp;
        client_port_udp = portUdp;
    }

    /** Initializing OpenGL functions */
    private void initOpenGl() {

        if ( !glfwInit() ){
            throw new IllegalStateException("Unable to initialize GLFW");}
        window = glfwCreateWindow(DISPLAY_WIDTH, DISPLAY_HEIGHT, "Pong - LWJGL3", 0, 0);
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);


        if(window == 0) {
            throw new RuntimeException("Failed to create window"); }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        System.out.println("OK");
    }

    /** Setting up screen, establishing connections (TCP, UPD) with server, etc. */
    private void init() {
        connections = new TcpConnection(this, server_ip, server_port_tcp);

        if ((ID = connections.getIdFromServer()) == -1) {
            System.err.println("cant get id for char");
        }

        obstacles = connections.getMapDetails();

        character = new CharacterObj(0, 0, ID);
        bullets = new ArrayList<>();
        camera = new Camera(0, 0);
        movingObjects = new ArrayList<>();

        System.out.println("OK");
        new Thread(new UdpConnection(this, connections, client_port_udp)).start();
        System.out.println("OK");


    }

    /** Game loop */
    private void start() {
        sendCharacter();

        glfwSetKeyCallback(window, (keyCallback = new GLFWKeyCallback() {

            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
                    character.xVel = 0;
                    character.yVel = 0;
                    sendCharacter();
                } else if(key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    closingOperations();
                } else if(key == GLFW_KEY_W && action == GLFW_PRESS) {
                    character.yVel = -5;
                    sendCharacter();
                } else if(key == GLFW_KEY_A && action == GLFW_PRESS) {
                    character.xVel = -5;
                    sendCharacter();
                } else if(key == GLFW_KEY_S && action == GLFW_PRESS) {
                    character.yVel = 5;
                    sendCharacter();
                } else if(key == GLFW_KEY_D && action == GLFW_PRESS) {
                    character.xVel = 5;
                    sendCharacter();
                }else{
                    character.xVel = 0;
                    character.yVel = 0;
                    sendCharacter();
                }
            }
        }));

        glfwSetMouseButtonCallback(window, (mouseButtonCallback = new GLFWMouseButtonCallback() {

            @Override
            public void invoke(long window, int button, int action, int mods) {
                if(button == 0) {
                    //If this event is down event and no current to-add-ball.
                    //Else If this event is up event and there is a current to-add-ball.
                    if(action == GLFW_PRESS) {
                        double[] xpos = new double[1];
                        double[] ypos = new double[1];
                        glfwGetCursorPos(window,xpos,ypos);

                        float xmouse = (float) xpos[0];
                        float ymouse = (float) ypos[0];
                        float pnx = 1;
                        float xmain = updatedCharacter.x + updatedCharacter.w  / 2;
                        float ymain = updatedCharacter.y + updatedCharacter.h / 2;
                        if (xmouse > xmain) {
                            pnx = -1;
                        }

                        float k = (ymain - ymouse) / (xmain - xmouse);
                        float c = ymain - k * xmain;
                        bullets.add(new Bullet(xmain, ymain, k, c, pnx));
                        sendCharacter();
                    }
                }
            }

        }));

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);
            handlingEvents();
            update();
            Sync.sync(30);
            render();

            glfwPollEvents();
            glfwSwapBuffers(window);

        }
        closingOperations();
    }


    /** Updating camera's position */
    private void update() {

        if (updatedCharacter != null) {
            camera.update(updatedCharacter);
        }
    }


    /** Rendering obstacles, players and bullets */
    private void render() {

        glTranslatef(-camera.xmov, -camera.ymov, 0);	//camera's position

        for (Box box : obstacles) {
            drawSquare(box);
        }
        for (Box box : movingObjects) {
            drawSquare(box);
        }
    }

    /** Function to draw square */
    private void drawSquare(Box box) {

        glColor3f(box.r, box.g, box.b);
        glBegin(GL_QUADS);
        glVertex2f(box.x, box.y);
        glVertex2f(box.x + box.w, box.y);
        glVertex2f(box.x + box.w, box.y + box.h);
        glVertex2f(box.x, box.y + box.h);
        glEnd();
    }

    /** Function to send main characters data to server */
    private void sendCharacter() {

        character.newBullets = bullets;
        connections.sendUpdatedVersion(character);
        bullets.clear();
    }

    /** Closing game */
    private void closingOperations() {

        connections.removeCharacter(ID);
        glfwDestroyWindow(window);
        System.exit(0);
    }

    /**
     * Getting info about game play
     *
     * @param objects Object can be either bullet or player
     */
    void updateListOfObjects(List<Box> objects) {
        if (objects == null)	return;
        movingObjects = objects;
        for (Box box : objects) {
            if (box.id == ID) {
                updatedCharacter = box;
                break;
            }
        }
    }



    private void handlingEvents() {

    }

    /**
     * Camera shows map regarding main character's position
     */
    private class Camera {

        private float x;
        private float y;

        private float xmov;
        private float ymov;

        Camera(float x, float y) {

            this.x = x;
            this.y = y;
            xmov = 0;
            ymov = 0;
        }

        private void update(Box character) {
            float xnew = character.x, ynew = character.y;
            float xCam = Math.min(Math.max(0, (xnew + character.w / 2) - DISPLAY_WIDTH / 2),
                    MAP_WIDTH - DISPLAY_WIDTH);
            float yCam = Math.min(Math.max(0, (ynew + character.h / 2) - DISPLAY_HEIGHT / 2),
                    MAP_HEIGHT - DISPLAY_HEIGHT);

            xmov = xCam - x;
            x = xCam;

            ymov = yCam - y;
            y = yCam;
        }
    }




}

