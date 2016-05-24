/*
 * Author: Jervin Cruz
 * Title: Particle Explosion Simulation
 
 * Purpose: When spacebar pressed, spinning cube explodes into thousands of colored particles moving outwardly
 *          with varying velocity.
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <GLUT/glut.h>
#include <time.h>


int   cube_exploded = 0;
float angle = 0.0; // camera rotation angle

float velocity = 0.3;
float s = -10.0;
float t = 0;       // time

// displacements
float x,y,z = 0;
float l,m,n = 0;

// color
float red = 1.0f;
float green = 1.0f;
float blue = 1.0f;

// glTranslate
float a,b = 0;
float c = -10;

// vector coords
typedef struct vec3{
    float x;
    float y;
    float z;
} vec3;

typedef struct Particle{
    vec3 position, speed;
} Particle;

const int NumParticles = 10000;
Particle p[NumParticles];
Particle p2[NumParticles];

// Light sources
GLfloat  light0Amb[4] =  { 1.0, 0.6, 0.2, 1.0 };
GLfloat  light0Dif[4] =  { 1.0, 0.6, 0.2, 1.0 };
GLfloat  light0Spec[4] = { 0.0, 0.0, 0.0, 1.0 };
GLfloat  light0Pos[4] =  { 0.0, 0.0, 0.0, 1.0 };
GLfloat  light1Amb[4] =  { 0.0, 0.0, 0.0, 1.0 };
GLfloat  light1Dif[4] =  { 1.0, 1.0, 1.0, 1.0 };
GLfloat  light1Spec[4] = { 1.0, 1.0, 1.0, 1.0 };
GLfloat  light1Pos[4] =  { 0.0, 5.0, 5.0, 0.0 };

void display (void);
void keyboard (unsigned char, int, int);
void reshape (int, int);
void idle (void);
void explode_cube (void);

void display (void)
{
 /*
     glBegin(GL_POINTS);
     glColor3f();
     glVertex3f();
     glEnd();
     glutSwapBuffers();
     
 */
    
    angle += 0.3;  /* Always continue to rotate the camera */

    glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glLoadIdentity ();
    
    //---------------------------------//
        glTranslatef(a,b,c);
        glRotatef (angle, 0.0, 1.0, 0.0);
    //---------------------------------//
    

    // Assign vertex values
    for(int i = 0; i < NumParticles; i++){
        x = rand() / (double) RAND_MAX * t;
        y = rand() / (double) RAND_MAX * t;
        z = rand() / (double) RAND_MAX * t;
        p[i].position.x = x;
        p[i].position.y = y;
        p[i].position.z = -z;
    }
    
    for(int i = 0; i < NumParticles; i++){
        l = rand() / (double) RAND_MAX * t;
        m = rand() / (double) RAND_MAX * t;
        n = rand() / (double) RAND_MAX * t;
        p2[i].position.x = l;
        p2[i].position.y = m;
        p2[i].position.z = -n;
    }
    
    // If no explosion, draw cube
    if (!cube_exploded)
    {
        glEnable (GL_LIGHTING);
        glDisable (GL_LIGHT0);
        glEnable (GL_DEPTH_TEST);
        glutSolidCube (1.0);
    }
    else{
        glEnable(GL_COLOR_MATERIAL);
        glBegin(GL_POINTS);
        for(int i = 0; i < NumParticles; i++){
        glVertex3f(p[i].position.x,p[i].position.y,p[i].position.z);
        glVertex3f(p2[i].position.x,-p2[i].position.y,p2[i].position.z);
        }
        glEnd();
    }
    glutSwapBuffers ();
    
}

void idle (void)
{
    /*
     1) Increment time here
     2) Iterate over all particles updating displacement
     s=vt   // velocity is constant, time is changing, compute the dist of each particle
     // your time will be a global variable
     3) glutpostRedisplay()
     
     */
    
    int i = 0;
    
    srand(time(NULL));
    
    if(cube_exploded == 1){
        
        // Change colors over time, ultimately fade to background
        red -= 0.0002;
        green -= 0.003;
        blue -= 0.05;
        
        glColor3f(red,green,blue);
        
        // translation & angle
        a = 0;
        b = 0;
        c += .2;
        angle = 40;
        
        t+= 1;
        while(i != 10000){
            p[i].position.x = velocity * t;
            p[i].position.y = velocity * t;
            p[i].position.z = velocity * t;
            i++;
        }
    }
    glutPostRedisplay ();
}

void keyboard (unsigned char key, int x, int y)
{
    switch (key)
    {
        case ' ':           // spacebar explodes cube
            explode_cube();
            //exit (0);
            break;
        case 27:
            exit (0);
            break;
    }
}

void reshape (int w, int h)
{

    glViewport (0.0, 0.0, (GLfloat) w, (GLfloat) h);
    glMatrixMode (GL_PROJECTION);
    glLoadIdentity ();
    gluPerspective (45.0, (GLfloat) w / (GLfloat) h, 0.1, 100.0);
    glMatrixMode (GL_MODELVIEW);
}

void explode_cube(void)
{
    cube_exploded = 1;
}

int main (int argc, char *argv[])
{
    glutInit (&argc, argv);
    glutInitWindowPosition (0, 0);
    glutInitWindowSize (1280, 1024);
    glutInitDisplayMode (GLUT_RGB | GLUT_DEPTH | GLUT_DOUBLE);
    glutCreateWindow ("Particle explosion");
    glutKeyboardFunc (keyboard);
    glutIdleFunc (idle);
    glutDisplayFunc (display);
    glutReshapeFunc (reshape);
    glEnable (GL_LIGHT0);
    glEnable (GL_LIGHT1);
    glLightfv (GL_LIGHT0, GL_AMBIENT, light0Amb);
    glLightfv (GL_LIGHT0, GL_DIFFUSE, light0Dif);
    glLightfv (GL_LIGHT0, GL_SPECULAR, light0Spec);
    glLightfv (GL_LIGHT0, GL_POSITION, light0Pos);
    glLightfv (GL_LIGHT1, GL_AMBIENT, light1Amb);
    glLightfv (GL_LIGHT1, GL_DIFFUSE, light1Dif);
    glLightfv (GL_LIGHT1, GL_SPECULAR, light1Spec);
    glLightfv (GL_LIGHT1, GL_POSITION, light1Pos);
    glLightModelf (GL_LIGHT_MODEL_TWO_SIDE, GL_TRUE);
    glEnable (GL_NORMALIZE);
    glutMainLoop ();
    return 0;
}
