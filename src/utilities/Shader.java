package utilities;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import graphicslib3D.GLSLUtils;

/**
 * Created by arash on 10/29/2015.
 */
public class Shader {
    public int createShaderPrograms(GLAutoDrawable drawable,String vert,String frag)
    {
        int[] vertCompiled = new int[1];
        int[] fragCompiled = new int[1];
        int[] linked = new int[1];

        GL4 gl = (GL4) drawable.getGL();

        String vshaderSource[] = GLSLUtils.readShaderSource(vert);
        String fshaderSource[] = GLSLUtils.readShaderSource(frag);
        int lengths[];

        int vShader = gl.glCreateShader(GL4.GL_VERTEX_SHADER);
        int fShader = gl.glCreateShader(GL4.GL_FRAGMENT_SHADER);

        lengths = new int[vshaderSource.length];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = vshaderSource[i].length();
        }
        gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, lengths, 0);

        lengths = new int[fshaderSource.length];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = fshaderSource[i].length();
        }
        gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, lengths, 0);

        gl.glCompileShader(vShader);
        GLSLUtils.printOpenGLError(drawable);  // can use returned boolean
        gl.glGetShaderiv(vShader, GL4.GL_COMPILE_STATUS, vertCompiled, 0);
        if (vertCompiled[0] == 1) {
            System.out.println("vertex compilation success");
        } else {
            System.out.println("vertex compilation failed");
            GLSLUtils.printShaderInfoLog(drawable, vShader);
        }

        gl.glCompileShader(fShader);
        GLSLUtils.printOpenGLError(drawable);  // can use returned boolean
        gl.glGetShaderiv(fShader, GL4.GL_COMPILE_STATUS, fragCompiled, 0);
        if (fragCompiled[0] == 1) {
            System.out.println("fragment compilation success");
        } else {
            System.out.println("fragment compilation failed");
            GLSLUtils.printShaderInfoLog(drawable, fShader);
        }

        int vfprogram = gl.glCreateProgram();
        gl.glAttachShader(vfprogram, vShader);
        gl.glAttachShader(vfprogram, fShader);
        gl.glLinkProgram(vfprogram);
        GLSLUtils.printOpenGLError(drawable);
        gl.glGetProgramiv(vfprogram, GL4.GL_LINK_STATUS, linked, 0);
        if (linked[0] == 1) {
            System.out.println("linking succeeded");
        } else {
            System.out.println("linking failed");
            GLSLUtils.printProgramInfoLog(drawable, vfprogram);
        }
        return vfprogram;
    }
    public int createShaderPrograms(GLAutoDrawable drawable,String vert,String frag,String tessc,String tesse)
    {
        int[] vertCompiled = new int[1];
        int[] fragCompiled = new int[1];
	    int[] tesscCompiled = new int[1];
	    int[] tesseCompiled = new int[1];
        int[] linked = new int[1];

        GL4 gl = (GL4) drawable.getGL();

        String vshaderSource[] = GLSLUtils.readShaderSource(vert);
        String fshaderSource[] = GLSLUtils.readShaderSource(frag);
	    String tcshaderSource[] = GLSLUtils.readShaderSource(tessc);
	    String teshaderSource[] = GLSLUtils.readShaderSource(tesse);
        int lengths[];

        int vShader = gl.glCreateShader(GL4.GL_VERTEX_SHADER);
        int fShader = gl.glCreateShader(GL4.GL_FRAGMENT_SHADER);
	    int tcShader = gl.glCreateShader(GL4.GL_TESS_CONTROL_SHADER);
	    int teShader = gl.glCreateShader(GL4.GL_TESS_EVALUATION_SHADER);

        lengths = new int[vshaderSource.length];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = vshaderSource[i].length();
        }
        gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, lengths, 0);

        lengths = new int[fshaderSource.length];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = fshaderSource[i].length();
        }
        gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, lengths, 0);

	    lengths = new int[tcshaderSource.length];
	    for (int i = 0; i < lengths.length; i++) {
		    lengths[i] = tcshaderSource[i].length();
	    }
	    gl.glShaderSource(tcShader, tcshaderSource.length, tcshaderSource, lengths, 0);

	    lengths = new int[teshaderSource.length];
	    for (int i = 0; i < lengths.length; i++) {
		    lengths[i] = teshaderSource[i].length();
	    }
	    gl.glShaderSource(teShader, teshaderSource.length, teshaderSource, lengths, 0);

        gl.glCompileShader(vShader);
        GLSLUtils.printOpenGLError(drawable);  // can use returned boolean
        gl.glGetShaderiv(vShader, GL4.GL_COMPILE_STATUS, vertCompiled, 0);
        if (vertCompiled[0] == 1) {
            System.out.println("vertex compilation success");
        } else {
            System.out.println("vertex compilation failed");
            GLSLUtils.printShaderInfoLog(drawable, vShader);
        }

        gl.glCompileShader(fShader);
        GLSLUtils.printOpenGLError(drawable);  // can use returned boolean
        gl.glGetShaderiv(fShader, GL4.GL_COMPILE_STATUS, fragCompiled, 0);
        if (fragCompiled[0] == 1) {
            System.out.println("fragment compilation success");
        } else {
            System.out.println("fragment compilation failed");
            GLSLUtils.printShaderInfoLog(drawable, fShader);
        }

	    gl.glCompileShader(tcShader);
	    GLSLUtils.printOpenGLError(drawable);  // can use returned boolean
	    gl.glGetShaderiv(tcShader, GL4.GL_COMPILE_STATUS, tesscCompiled, 0);
	    if (tesscCompiled[0] == 1) {
		    System.out.println("tesselation controller compilation success");
	    } else {
		    System.out.println("tesselation controller compilation failed");
		    GLSLUtils.printShaderInfoLog(drawable, tcShader);
	    }

	    gl.glCompileShader(teShader);
	    GLSLUtils.printOpenGLError(drawable);  // can use returned boolean
	    gl.glGetShaderiv(teShader, GL4.GL_COMPILE_STATUS, tesseCompiled, 0);
	    if (tesseCompiled[0] == 1) {
		    System.out.println("tesselation evaluation compilation success");
	    } else {
		    System.out.println("tesselation evaluation compilation failed");
		    GLSLUtils.printShaderInfoLog(drawable, teShader);
	    }

        int vfprogram = gl.glCreateProgram();
        gl.glAttachShader(vfprogram, vShader);
        gl.glAttachShader(vfprogram, fShader);
	    gl.glAttachShader(vfprogram, tcShader);
	    gl.glAttachShader(vfprogram, teShader);
        gl.glLinkProgram(vfprogram);
        GLSLUtils.printOpenGLError(drawable);
        gl.glGetProgramiv(vfprogram, GL4.GL_LINK_STATUS, linked, 0);
        if (linked[0] == 1) {
            System.out.println("linking succeeded");
        } else {
            System.out.println("linking failed");
            GLSLUtils.printProgramInfoLog(drawable, vfprogram);
        }
        return vfprogram;
    }
    public int createShaderPrograms(GLAutoDrawable drawable,String vert,String frag,String geom)
     {
        int[] vertCompiled = new int[1];
        int[] fragCompiled = new int[1];
        int[] geomCompiled = new int[1];
        int[] linked = new int[1];

        GL4 gl = (GL4) drawable.getGL();

        String vshaderSource[] = GLSLUtils.readShaderSource(vert);
        String fshaderSource[] = GLSLUtils.readShaderSource(frag);
        String gshaderSource[] = GLSLUtils.readShaderSource(geom);
        int lengths[];

        int vShader = gl.glCreateShader(GL4.GL_VERTEX_SHADER);
        int fShader = gl.glCreateShader(GL4.GL_FRAGMENT_SHADER);
        int gShader = gl.glCreateShader(GL4.GL_GEOMETRY_SHADER);

        lengths = new int[vshaderSource.length];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = vshaderSource[i].length();
        }
        gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, lengths, 0);

        lengths = new int[fshaderSource.length];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = fshaderSource[i].length();
        }
        gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, lengths, 0);

        lengths = new int[gshaderSource.length];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = gshaderSource[i].length();
        }
        gl.glShaderSource(gShader, gshaderSource.length, gshaderSource, lengths, 0);


        gl.glCompileShader(vShader);
        GLSLUtils.printOpenGLError(drawable);  // can use returned boolean
        gl.glGetShaderiv(vShader, GL4.GL_COMPILE_STATUS, vertCompiled, 0);
        if (vertCompiled[0] == 1) {
            System.out.println("vertex compilation success");
        } else {
            System.out.println("vertex compilation failed");
            GLSLUtils.printShaderInfoLog(drawable, vShader);
        }

        gl.glCompileShader(fShader);
        GLSLUtils.printOpenGLError(drawable);  // can use returned boolean
        gl.glGetShaderiv(fShader, GL4.GL_COMPILE_STATUS, fragCompiled, 0);
        if (fragCompiled[0] == 1) {
            System.out.println("fragment compilation success");
        } else {
            System.out.println("fragment compilation failed");
            GLSLUtils.printShaderInfoLog(drawable, fShader);
        }

        gl.glCompileShader(gShader);
        GLSLUtils.printOpenGLError(drawable);  // can use returned boolean
        gl.glGetShaderiv(gShader, GL4.GL_COMPILE_STATUS, geomCompiled, 0);
        if (geomCompiled[0] == 1) {
            System.out.println("geometry controller compilation success");
        } else {
            System.out.println("geometry controller compilation failed");
            GLSLUtils.printShaderInfoLog(drawable, gShader);
        }



        int vfprogram = gl.glCreateProgram();
        gl.glAttachShader(vfprogram, vShader);
        gl.glAttachShader(vfprogram, fShader);
        gl.glAttachShader(vfprogram, gShader);
        gl.glLinkProgram(vfprogram);
        GLSLUtils.printOpenGLError(drawable);
        gl.glGetProgramiv(vfprogram, GL4.GL_LINK_STATUS, linked, 0);
        if (linked[0] == 1) {
            System.out.println("linking succeeded");
        } else {
            System.out.println("linking failed");
            GLSLUtils.printProgramInfoLog(drawable, vfprogram);
        }
        return vfprogram;
    }
}
