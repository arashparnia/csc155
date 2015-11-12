package shapes;

import graphicslib3D.Vertex3D;

/**
 * Created by arash on 11/8/2015.
 */
public class Cube {
    private int[] indices;
    private Vertex3D[] vertices;
    public Cube() {
        //InitCube();
    }

    private float[] fvalues = {
            -0.25f,  0.25f, -0.25f, -0.25f, -0.25f, -0.25f, 0.25f, -0.25f, -0.25f,
            0.25f, -0.25f, -0.25f, 0.25f,  0.25f, -0.25f, -0.25f,  0.25f, -0.25f,

            0.25f, -0.25f, -0.25f, 0.25f, -0.25f,  0.25f, 0.25f,  0.25f, -0.25f,
            0.25f, -0.25f,  0.25f, 0.25f,  0.25f,  0.25f, 0.25f,  0.25f, -0.25f,

            0.25f, -0.25f,  0.25f, -0.25f, -0.25f,  0.25f, 0.25f,  0.25f,  0.25f,
            -0.25f, -0.25f,  0.25f, -0.25f,  0.25f,  0.25f, 0.25f,  0.25f,  0.25f,

            -0.25f, -0.25f,  0.25f, -0.25f, -0.25f, -0.25f, -0.25f,  0.25f,  0.25f,
            -0.25f, -0.25f, -0.25f, -0.25f,  0.25f, -0.25f, -0.25f,  0.25f,  0.25f,

            -0.25f, -0.25f,  0.25f,  0.25f, -0.25f,  0.25f,  0.25f, -0.25f, -0.25f,
            0.25f, -0.25f, -0.25f, -0.25f, -0.25f, -0.25f, -0.25f, -0.25f,  0.25f,

            -0.25f,  0.25f, -0.25f, 0.25f,  0.25f, -0.25f, 0.25f,  0.25f,  0.25f,
            0.25f,  0.25f,  0.25f, -0.25f,  0.25f,  0.25f, -0.25f,  0.25f, -0.25f
    };
    private float[] nvalues = {
            //back
            0,0,-1,0,0,-1,0,0,-1,
            0,0,-1,0,0,-1,0,0,-1,
            //right
            1,0,0,1,0,0,1,0,0,
            1,0,0,1,0,0,1,0,0,
            //front
            0,0,1,0,0,1,0,0,1,
            0,0,1,0,0,1,0,0,1,
            //left
            -1,0,0,-1,0,0,-1,0,0,
            -1,0,0,-1,0,0,-1,0,0,
            //bottom
            0,-1,0,0,-1,0,0,-1,0,
            0,-1,0,0,-1,0,0,-1,0,
            //top
            0,1,0,0,1,0,0,1,0,
            0,1,0,0,1,0,0,1,0,
    };

    private float[] tvalues = {
            1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,//back
            1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,//rside
            1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, //front
            0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,//leftside
            0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, //bottom
            0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f //top


    };

    public float[] getFValues(){return  fvalues;}
    public float[] getTValues(){return  tvalues;}
    public float[] getNValues(){return  nvalues;}
}
