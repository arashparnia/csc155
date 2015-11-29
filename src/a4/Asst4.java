package a4;

import graphicslib3D.*;
import graphicslib3D.light.*;
import graphicslib3D.GLSLUtils.*;

import java.awt.*;
import java.awt.event.*;
import java.nio.*;
import java.util.Random;
import javax.swing.*;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import graphicslib3D.shape.*;
import com.jogamp.opengl.util.*;
import models.ImportedModel;
import shapes.Cube;
import utilities.*;

public class Asst4 extends JFrame implements GLEventListener, ActionListener, MouseListener,MouseWheelListener,MouseMotionListener, KeyListener{

	graphicslib3D.Material thisMaterial;
	private GLCanvas myCanvas;
	private int rendering_program1, rendering_program2,rendering_program3;
	private int mv_location, proj_location, vertexLoc, n_location,shadow_location;
	float aspect;
	private GLSLUtils util = new GLSLUtils();


	// shadow stuff
	int scSizeX, scSizeY;
	int [] shadow_tex = new int [1];
	int [] shadow_buffer = new int [1];
	Matrix3D lightV_matrix = new Matrix3D();
	Matrix3D lightP_matrix = new Matrix3D();
	Matrix3D shadowMVP1 = new Matrix3D();
	Matrix3D shadowMVP2 = new Matrix3D();
	Matrix3D b = new Matrix3D();




	private boolean animated = true;
	private Point mousePoint= new Point();
	public static boolean axis = false;

	private Dimension dimention = new Dimension(1000, 1000);
	private int vao[] = new int[1];
	private int vbo[] = new int[200];

	private int rendering_program;
	private int rendering_program_axis;
	private int  rendering_program_no_lighting;
	private int rendering_program_blinnphong_lighting;


	private utilities.Animator animator;

	private Random rand;




	//----------------------------------------------------------------------------------------OBJECTS
	private Cube cube = new Cube();
	//private shapes.Ground ground = new shapes.Ground(1000);
	private shapes.Sphere mySphere = new shapes.Sphere(48);
	private shapes.Astroid rock = new shapes.Astroid(100);
	//private shapes.Astroid rock1 = new shapes.Astroid(100,2,3000,0.01f);
	//private shapes.Astroid rock2 = new shapes.Astroid(100,1,2000,0.01f);
	private ImportedModel grassModel = new ImportedModel("Grass_02.obj");
	private ImportedModel myModel = new ImportedModel("Tiger.obj");
	//------------------------------------------------------------------------------------------MATRICIES
	private Matrix3D m_matrix = new Matrix3D();
	private Matrix3D v_matrix = new Matrix3D();
	private Matrix3D mv_matrix = new Matrix3D();
	private Matrix3D proj_matrix = new Matrix3D();
	//-------------------------------------------------------------------------------------------CAMERA
	public static float zoom = 0.0f;
	public static float pan = 0.0f;
	public static float pitch = 0.0f;
	public static float strafe = 0.0f;
	private float upDown = 0.0f;
	private Vector3D u = new Vector3D(1, 0, 0);
	private Vector3D v = new Vector3D(0, 1, 0);
	private Vector3D n = new Vector3D(0, 0, 1 );
	private Vector3D xyz = new Vector3D(0,0,0);
	//------------------------------------------------------------------------------------------TEXTURE
	private TextureReader tr = new TextureReader();
	private  int grassTexture,tigerTexture,rockTexture,waterTexture,heightTexture,normalTexture,textureID0,textureID1,textureID2;
	//-------------------------------------------------------------------------------------------MATERIALS
	private float[] rockambient = {0.0f,0.0f,0.0f,1.0f};
	private float[] rockdiffuse = {0.1f,0.1f,0.1f,1.0f};
	private float[] rockspecular =  {0.1f,0.1f,0.1f,1.0f};
	private float[] rockemission = {0.1f,0.1f,0.1f,1.0f};
	private float rockshininess = 0.0f;
	graphicslib3D.Material rockMaterial = new Material("rock",rockambient,rockdiffuse,rockspecular,rockemission,rockshininess);
	private float[] grassambient = {0.0f,0.0f,0.0f,1.0f};
	private float[] grassdiffuse = {0.1f,0.35f,0.1f,1.0f};
	private float[] grassspecular =  {0.45f,0.55f,0.45f,1.0f};
	private float[] grassemission = {0.1f,0.1f,0.1f,1.0f};
	private float grassshininess = 10f;
	graphicslib3D.Material grassMaterial = new Material("grass",grassambient,grassdiffuse,grassspecular,grassemission,grassshininess);
	private float[] sunambient = {1.0f,1.0f,1.0f,1.0f};
	private float[] sundiffuse = {1.0f,1.0f,1.0f,1.0f};
	private float[] sunspecular =  {1.0f,1.0f,1.0f,1.0f};
	private float[] sunemission = {1.0f,1.0f,1.0f,1.0f};
	private float sunshininess = 10f;
	graphicslib3D.Material sunMaterial = new Material("sun",sunambient,sundiffuse,sunspecular,sunemission,sunshininess);
	private float[] skinambient = {0.3f,0.2f,0.2f,1.0f};
	private float[] skindiffuse = {1.0f,0.9f,0.8f,1.0f};
	private float[] skinspecular =  {0.4f,0.2f,0.2f,1.0f};
	private float[] skinemission = {1.0f,1.0f,1.0f,1.0f};
	private float skinshininess = 44.8f;
	graphicslib3D.Material skinMaterial = new Material("skin",skinambient,skindiffuse,skinspecular,skinemission,skinshininess);
	private float[] waterambient = {0.2f,0.2f,0.6f,1.0f};
	private float[] waterdiffuse = {0.8f,0.9f,1.0f,1.0f};
	private float[] waterspecular =  {0.2f,0.2f,0.4f,1.0f};
	private float[] wateremission = {1.0f,1.0f,1.0f,1.0f};
	private float watershininess = 50f;
	graphicslib3D.Material waterMaterial = new Material("water",waterambient,waterdiffuse,waterspecular,wateremission,watershininess);
	//-------------------------------------------------------------------------------------------------LIGHT
	private int lights = 1;
	private PositionalLight currentLight = new PositionalLight();
	private Point3D lightLoc = new Point3D(15f,40f,15f);
	float [] globalAmbient = new float[] { 0.1f, 0.1f, 0.1f, 1.0f };

	public Asst4() {
		setTitle("Assignment 4 CSC155");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);
		myCanvas = new GLCanvas();
		myCanvas.addGLEventListener(this);
		add(myCanvas);
		myCanvas.addKeyListener(this);
		this.addKeyListener(this);
		myCanvas.addMouseWheelListener(this);
		myCanvas.addMouseMotionListener(this);
		myCanvas.addMouseListener(this);
		myCanvas.requestFocus();
		keyMaping();
		setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		FPSAnimator animator = new FPSAnimator(myCanvas, 30);
		animator.start();
	}
	public void init(GLAutoDrawable drawable) {
		GL4 gl = (GL4) drawable.getGL();
		Shader sh = new Shader();

		rendering_program1 = sh.createShaderPrograms(drawable,"shaders/Vert1.glsl","shaders/Frag1.glsl");
		rendering_program2 = sh.createShaderPrograms(drawable,"shaders/Vert2.glsl","shaders/Frag2.glsl");
		rendering_program3 = sh.createShaderPrograms(drawable,"shaders/Vert3.glsl","shaders/Frag2.glsl","shaders/TessC3.glsl","shaders/TessE3.glsl");

		setupVertices(gl);
		setupShadowBuffers(drawable);
		b.setElementAt(0,0,0.5);b.setElementAt(0,1,0.0);b.setElementAt(0,2,0.0);b.setElementAt(0,3,0.5f);
		b.setElementAt(1,0,0.0);b.setElementAt(1,1,0.5);b.setElementAt(1,2,0.0);b.setElementAt(1,3,0.5f);
		b.setElementAt(2,0,0.0);b.setElementAt(2,1,0.0);b.setElementAt(2,2,0.5);b.setElementAt(2,3,0.5f);
		b.setElementAt(3,0,0.0);b.setElementAt(3,1,0.0);b.setElementAt(3,2,0.0);b.setElementAt(3,3,1.0f);
		// may reduce shadow border artifacts
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		rockTexture = tr.loadTexture(drawable, "textures/rock.jpg");
		grassTexture = tr.loadTexture(drawable, "textures/grass.jpg");

		textureID0 = tr.loadTexture(drawable, "textures/moon.jpg");
		textureID1 = tr.loadTexture(drawable, "textures/height.jpg");
		textureID2 = tr.loadTexture(drawable, "textures/normal.jpg");

		xyz.setZ(25);
		xyz.setY(20);
		Matrix3D r = new Matrix3D();
		r.rotate(-40, u.normalize());
		n = n.mult(r);
		v = v.mult(r);
	}
	private void transformRock(Matrix3D m){
		m.setToIdentity();
		m.translate(-5,5,5);
		m.scale(1,5,2);
		//m.rotate(180,0,1,1);
	}
	private void transformSphere(Matrix3D m){
		m.setToIdentity();
		m.translate(lightLoc.getX(),lightLoc.getY(),lightLoc.getZ());
		m.scale(.1,.1,.1);
	}
	private  void transformGrassModel(Matrix3D m){
		m.setToIdentity();
		m.translate(-20,0,-20);
		m.scale(1,2,1);
		//m.rotate(180,0,1,1);
	}
	private  void transformTheModel(Matrix3D m){
		m.setToIdentity();
		double amt = (double)(System.currentTimeMillis()%36000)/100.0;
		m.translate(0,5,0);
		m.scale(0.001,0.001,0.001);
		m.rotate(amt,new Vector3D(0,1,0));
	}

	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) drawable.getGL();

		double amt = (double)(System.currentTimeMillis()%36000)/10000.0;
		lightLoc.setX(Math.cos(amt)*20);
		lightLoc.setZ(Math.sin(amt)*20);

		currentLight.setPosition(lightLoc);
		aspect = myCanvas.getWidth() / myCanvas.getHeight();
		proj_matrix = perspective(50.0f, aspect, 0.1f, 1000.0f);

		FloatBuffer bg = FloatBuffer.allocate(4);
		bg.put(0, 0.0f); bg.put(1, 0.0f); bg.put(2, 0.2f); bg.put(3, 1.0f);
		gl.glClearBufferfv(GL_COLOR,0,bg);

		float depthClearVal[] = new float[1]; depthClearVal[0] = 1.0f;
		gl.glClearBufferfv(GL_DEPTH,0,depthClearVal,0);

		gl.glBindFramebuffer(GL_FRAMEBUFFER, shadow_buffer[0]);
		gl.glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, shadow_tex[0], 0);

		gl.glDrawBuffer(GL.GL_NONE);
		gl.glEnable(GL_DEPTH_TEST);

		gl.glEnable(GL_POLYGON_OFFSET_FILL);	// for reducing
		gl.glPolygonOffset(2f,  4f);			//  shadow artifacts

		firstPass(drawable);

		gl.glDisable(GL_POLYGON_OFFSET_FILL);	// artifact reduction, continued

		gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
		gl.glActiveTexture(gl.GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, shadow_tex[0]);

		gl.glDrawBuffer(GL.GL_FRONT);

		secondPass(drawable);
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ PASS 1
 	public void firstPass(GLAutoDrawable drawable) {
		GL4 gl = (GL4) drawable.getGL();
		gl.glUseProgram(rendering_program1);
		shadow_location = gl.glGetUniformLocation(rendering_program1, "shadowMVP");
		mv_location = gl.glGetUniformLocation(rendering_program1, "mv_matrix");
		proj_location = gl.glGetUniformLocation(rendering_program1, "proj_matrix");

		lightV_matrix.setToIdentity();lightP_matrix.setToIdentity();
		lightV_matrix = lookAt(lightLoc, new Point3D(0.0, 0.0, 0.0), new Vector3D(0.0, 1.0, 0.0));	// vector from light to origin
		lightP_matrix = perspective(50.0f, aspect, 0.1f, 1000.0f);

		//=================================================================draw the rock PASS 1

		transformRock(m_matrix);

		shadowMVP1.setToIdentity();shadowMVP1.concatenate(lightP_matrix);shadowMVP1.concatenate(lightV_matrix);shadowMVP1.concatenate(m_matrix);
		gl.glUniformMatrix4fv(shadow_location, 1, false, shadowMVP1.getFloatValues(), 0);

		// set up torus vertices buffer
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[20]);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);

		gl.glClear(GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL_CULL_FACE);
		gl.glFrontFace(GL_CCW);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);

		gl.glDrawArrays(GL_TRIANGLES, 0, rock.getIndices().length);
		// ---------------------------------------------------------------------draw the grassmodel PASS 1
		transformGrassModel(m_matrix);

		shadowMVP1.setToIdentity();
		shadowMVP1.concatenate(lightP_matrix);
		shadowMVP1.concatenate(lightV_matrix);
		shadowMVP1.concatenate(m_matrix);
		gl.glUniformMatrix4fv(shadow_location, 1, false, shadowMVP1.getFloatValues(), 0);

		// set up vertices buffer
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[40]);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);

		gl.glEnable(GL_CULL_FACE);
		gl.glFrontFace(GL_CCW);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);

		gl.glDrawArraysInstanced(GL_TRIANGLES, 0, grassModel.getIndices().length,8*8);
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ PASS 2
	public void secondPass(GLAutoDrawable drawable) {
		GL4 gl = (GL4) drawable.getGL();
		gl.glUseProgram(rendering_program2);
		mv_location = gl.glGetUniformLocation(rendering_program2, "mv_matrix");
		proj_location = gl.glGetUniformLocation(rendering_program2, "proj_matrix");
		n_location = gl.glGetUniformLocation(rendering_program2, "normalMat");
		shadow_location = gl.glGetUniformLocation(rendering_program2,  "shadowMVP");

		//------------------------------------------------------------------SETTING CAMERA PASS 2
		v_matrix.setToIdentity();
		v_matrix.concatenate(getUVNCamera());
		m_matrix.setToIdentity();

		//================================================================== draw the rock PASS 2

		thisMaterial = Material.GOLD;
		installLights(rendering_program2, v_matrix, drawable);
		//  build the MODEL matrix
		transformRock(m_matrix);
		// shadow matrix
		shadowMVP2.setToIdentity();
		shadowMVP2.concatenate(b);
		shadowMVP2.concatenate(lightP_matrix);
		shadowMVP2.concatenate(lightV_matrix);
		shadowMVP2.concatenate(m_matrix);

		//  build the MODEL-VIEW matrix
		mv_matrix.setToIdentity();
		mv_matrix.concatenate(v_matrix);
		mv_matrix.concatenate(m_matrix);
		//  put the MV and PROJ matrices into the corresponding uniforms
		gl.glUniformMatrix4fv(mv_location, 1, false, mv_matrix.getFloatValues(), 0);
		gl.glUniformMatrix4fv(proj_location, 1, false, proj_matrix.getFloatValues(), 0);
		gl.glUniformMatrix4fv(n_location, 1, false, (mv_matrix.inverse()).transpose().getFloatValues(), 0);
		gl.glUniformMatrix4fv(shadow_location, 1, false, shadowMVP2.getFloatValues(), 0);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[20]);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[21]);
		gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(1);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[22]);
		gl.glVertexAttribPointer(2, 2, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(2);

		gl.glActiveTexture(GL_TEXTURE1);
		gl.glBindTexture(GL_TEXTURE_2D, rockTexture);
		gl.glGenerateMipmap(GL_TEXTURE_2D);
		gl.glActiveTexture(GL_TEXTURE0);

		gl.glClear(GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL_CULL_FACE);
		gl.glFrontFace(GL_CCW);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);

		gl.glDrawArrays(GL_TRIANGLES, 0, rock.getIndices().length);
		// ==================================================================draw the grass PASS 2
		thisMaterial = grassMaterial;
		installLights(rendering_program2, v_matrix, drawable);

		//  build the MODEL matrix
		transformGrassModel(m_matrix);
		//  build the shadow
		shadowMVP2.setToIdentity();
		shadowMVP2.concatenate(b);
		shadowMVP2.concatenate(lightP_matrix);
		shadowMVP2.concatenate(lightV_matrix);
		shadowMVP2.concatenate(m_matrix);

		//  build the MODEL-VIEW matrix
		mv_matrix.setToIdentity();
		mv_matrix.concatenate(v_matrix);
		mv_matrix.concatenate(m_matrix);

		//  put the MV and PROJ matrices into the corresponding uniforms
		gl.glUniformMatrix4fv(mv_location, 1, false, mv_matrix.getFloatValues(), 0);
		gl.glUniformMatrix4fv(proj_location, 1, false, proj_matrix.getFloatValues(), 0);
		gl.glUniformMatrix4fv(n_location, 1, false, (mv_matrix.inverse()).transpose().getFloatValues(), 0);
		gl.glUniformMatrix4fv(shadow_location, 1, false, shadowMVP2.getFloatValues(), 0);

		// set up vertices buffer
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[40]);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);

		// set up normals buffer
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[41]);
		gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(1);

		// set up texture buffer
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[42]);
		gl.glVertexAttribPointer(2, 2, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(2);

		gl.glEnable(GL_CULL_FACE);
		gl.glFrontFace(GL_CCW);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);

		gl.glActiveTexture(GL_TEXTURE1);
		gl.glBindTexture(GL_TEXTURE_2D, grassTexture);
		gl.glGenerateMipmap(GL_TEXTURE_2D);


		gl.glDrawArraysInstanced(GL_TRIANGLES, 0, grassModel.getIndices().length,8*8);

//================================================================== draw the LIGHT PASS 2

		thisMaterial = sunMaterial;
		installLights(rendering_program2, v_matrix, drawable);

		//  build the MODEL matrix

		transformSphere(m_matrix);
		//shadow
		shadowMVP2.setToIdentity();
		shadowMVP2.concatenate(b);
		shadowMVP2.concatenate(lightP_matrix);
		shadowMVP2.concatenate(lightV_matrix);
		shadowMVP2.concatenate(m_matrix);

		//  build the MODEL-VIEW matrix
		mv_matrix.setToIdentity();
		mv_matrix.concatenate(v_matrix);
		mv_matrix.concatenate(m_matrix);
		//  put the MV and PROJ matrices into the corresponding uniforms
		gl.glUniformMatrix4fv(mv_location, 1, false, mv_matrix.getFloatValues(), 0);
		gl.glUniformMatrix4fv(proj_location, 1, false, proj_matrix.getFloatValues(), 0);
		gl.glUniformMatrix4fv(n_location, 1, false, (mv_matrix.inverse()).transpose().getFloatValues(), 0);
		gl.glUniformMatrix4fv(shadow_location, 1, false, shadowMVP2.getFloatValues(), 0);


		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[0]);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[1]);
		gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(1);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[2]);
		gl.glVertexAttribPointer(2, 2, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(2);

		gl.glClear(GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL_CULL_FACE);
		gl.glFrontFace(GL_CCW);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);

		gl.glDrawArrays(GL_TRIANGLES, 0, mySphere.getIndices().length);

		//-----------------PROGRAM 3 ------------------------------------TESSELATION HIGHT MAP INSTANCED  PASS 2
		gl.glUseProgram(rendering_program3);


		int mvp_location = gl.glGetUniformLocation(rendering_program, "mvp");
		int mv_location = gl.glGetUniformLocation(rendering_program, "mv_matrix");
		int proj_location = gl.glGetUniformLocation(rendering_program, "proj_matrix");
		int n_location = gl.glGetUniformLocation(rendering_program, "normalMat");


		m_matrix.setToIdentity();
		m_matrix.translate(0,0,0);
		m_matrix.rotateX(20.0f);


		mv_matrix.setToIdentity();
		mv_matrix.concatenate(v_matrix);
		mv_matrix.concatenate(m_matrix);

		installLights(rendering_program3, v_matrix, drawable);


		mv_matrix.setToIdentity();
		mv_matrix.concatenate(v_matrix);
		mv_matrix.concatenate(m_matrix);
		//  put the MV and PROJ matrices into the corresponding uniforms
		gl.glUniformMatrix4fv(mv_location, 1, false, mv_matrix.getFloatValues(), 0);
		gl.glUniformMatrix4fv(proj_location, 1, false, proj_matrix.getFloatValues(), 0);
		gl.glUniformMatrix4fv(n_location, 1, false, (mv_matrix.inverse()).transpose().getFloatValues(), 0);

		gl.glActiveTexture(gl.GL_TEXTURE0);
		gl.glBindTexture(gl.GL_TEXTURE_2D, textureID0);
		gl.glActiveTexture(gl.GL_TEXTURE1);
		gl.glBindTexture(gl.GL_TEXTURE_2D, textureID1);
		gl.glActiveTexture(gl.GL_TEXTURE2);
		gl.glBindTexture(gl.GL_TEXTURE_2D, textureID2);

		gl.glClear(GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glFrontFace(GL_CCW);

		gl.glPatchParameteri(GL_PATCH_VERTICES, 4);
		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		gl.glDrawArraysInstanced(GL_PATCHES, 0, 4, 64*64);
	}


	private Matrix3D getUVNCamera() {
		Matrix3D uvnMatrix = new Matrix3D();
		uvnMatrix.setRow(0, u);
		uvnMatrix.setRow(1, v);
		uvnMatrix.setRow(2, n);
		uvnMatrix.setRow(3, new Vector3D(0, 0, 0, 1));
		Matrix3D t = new Matrix3D();
		t.setRow(0, new Vector3D(1, 0, 0, -xyz.getX()));
		t.setRow(1, new Vector3D(0, 1, 0, -xyz.getY()));
		t.setRow(2, new Vector3D(0, 0, 1, -xyz.getZ()));
		t.setRow(3, new Vector3D(0, 0, 0, 1));
		uvnMatrix.concatenate(t);
		return uvnMatrix;
	}
	public void setupShadowBuffers(GLAutoDrawable drawable) {
		GL4 gl = (GL4) drawable.getGL();

		scSizeX = myCanvas.getWidth();
		scSizeY = myCanvas.getHeight();

		gl.glGenFramebuffers(1, shadow_buffer, 0);
		gl.glBindFramebuffer(GL_FRAMEBUFFER, shadow_buffer[0]);

		gl.glGenTextures(1, shadow_tex, 0);
		gl.glBindTexture(GL_TEXTURE_2D, shadow_tex[0]);
		gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32,
				scSizeX, scSizeY, 0, GL_DEPTH_COMPONENT, GL_FLOAT, null);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_REF_TO_TEXTURE);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);
	}
	private void setupVerteciesCube(GL4 gl){
		float[] fvalues = cube.getFValues();
		float[] tvalues = cube.getTValues();
		float[] nvalues = cube.getNValues();

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[100]);
		FloatBuffer vertBuf = FloatBuffer.wrap(fvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertBuf.limit() * 4, vertBuf, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[101]);
		FloatBuffer norBuf = FloatBuffer.wrap(nvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, norBuf.limit() * 4, norBuf, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[102]);
		FloatBuffer texBuf = FloatBuffer.wrap(tvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL.GL_STATIC_DRAW);
	}//100,101,102
	private void setupVerticesSphere(GL4 gl){
		Vertex3D[] vertices = mySphere.getVertices();
		int[] indices = mySphere.getIndices();
		float[] fvalues = new float[indices.length * 3];
		float[] tvalues = new float[indices.length * 2];
		float[] nvalues = new float[indices.length * 3];
		for (int i = 0; i < indices.length; i++)
		{
			fvalues[i * 3] = (float) (vertices[indices[i]]).getX();
			fvalues[i * 3 + 1] = (float) (vertices[indices[i]]).getY();
			fvalues[i * 3 + 2] = (float) (vertices[indices[i]]).getZ();
			tvalues[i * 2] = (float) (vertices[indices[i]]).getS();
			tvalues[i * 2 + 1] = (float) (vertices[indices[i]]).getT();
			nvalues[i * 3] = (float) (vertices[indices[i]]).getNormalX();
			nvalues[i * 3 + 1] = (float) (vertices[indices[i]]).getNormalY();
			nvalues[i * 3 + 2] = (float) (vertices[indices[i]]).getNormalZ();
		}
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[0]);
		FloatBuffer vertBuf = FloatBuffer.wrap(fvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertBuf.limit() * 4, vertBuf, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[1]);
		FloatBuffer norBuf = FloatBuffer.wrap(nvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, norBuf.limit() * 4, norBuf, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[2]);
		FloatBuffer texBuf = FloatBuffer.wrap(tvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL.GL_STATIC_DRAW);
	}//00,01,02
	private void setupVerticesRock(GL4 gl) { // ROCK
		Vertex3D[] vertices = rock.getVertices();
		int[] indices = rock.getIndices();
		float[] fvalues = new float[indices.length * 3];
		float[] tvalues = new float[indices.length * 2];
		float[] nvalues = new float[indices.length * 3];
		for (int i = 0; i < indices.length; i++)
		{
			fvalues[i * 3] = (float) (vertices[indices[i]]).getX();
			fvalues[i * 3 + 1] = (float) (vertices[indices[i]]).getY();
			fvalues[i * 3 + 2] = (float) (vertices[indices[i]]).getZ();
			tvalues[i * 2] = (float) (vertices[indices[i]]).getS();
			tvalues[i * 2 + 1] = (float) (vertices[indices[i]]).getT();
			nvalues[i * 3] = (float) (vertices[indices[i]]).getNormalX();
			nvalues[i * 3 + 1] = (float) (vertices[indices[i]]).getNormalY();
			nvalues[i * 3 + 2] = (float) (vertices[indices[i]]).getNormalZ();
		}

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[20]);
		FloatBuffer vertBuf = FloatBuffer.wrap(fvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertBuf.limit() * 4, vertBuf, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[21]);
		FloatBuffer norBuf = FloatBuffer.wrap(nvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, norBuf.limit() * 4, norBuf, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[22]);
		FloatBuffer texBuf = FloatBuffer.wrap(tvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL.GL_STATIC_DRAW);
	}//20,21,22
	private void setupVerticesMyModel(GL4 gl){ // imported model
		Vertex3D[] vertices = myModel.getVertices();
		int[] indices = myModel.getIndices();
		float[] fvalues = new float[indices.length * 3];
		float[] tvalues = new float[indices.length * 2];
		float[] nvalues = new float[indices.length * 3];
		for (int i = 0; i < indices.length; i++)
		{
			fvalues[i * 3] = (float) (vertices[indices[i]]).getX();
			fvalues[i * 3 + 1] = (float) (vertices[indices[i]]).getY();
			fvalues[i * 3 + 2] = (float) (vertices[indices[i]]).getZ();
			tvalues[i * 2] = (float) (vertices[indices[i]]).getS();
			tvalues[i * 2 + 1] = (float) (vertices[indices[i]]).getT();
			nvalues[i * 3] = (float) (vertices[indices[i]]).getNormalX();
			nvalues[i * 3 + 1] = (float) (vertices[indices[i]]).getNormalY();
			nvalues[i * 3 + 2] = (float) (vertices[indices[i]]).getNormalZ();
		}

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[30]);
		FloatBuffer vertBuf = FloatBuffer.wrap(fvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertBuf.limit() * 4, vertBuf, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[31]);
		FloatBuffer norBuf = FloatBuffer.wrap(nvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, norBuf.limit() * 4, norBuf, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[32]);
		FloatBuffer texBuf = FloatBuffer.wrap(tvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL.GL_STATIC_DRAW);


	}//30,31,32
	private void setupVerticesGrassModel(GL4 gl) { // imported model
		Vertex3D[] vertices = grassModel.getVertices();
		int[] indices = grassModel.getIndices();
		float[] fvalues = new float[indices.length * 3];
		float[] tvalues = new float[indices.length * 2];
		float[] nvalues = new float[indices.length * 3];
		for (int i = 0; i < indices.length; i++)
		{
			fvalues[i * 3] = (float) (vertices[indices[i]]).getX();
			fvalues[i * 3 + 1] = (float) (vertices[indices[i]]).getY();
			fvalues[i * 3 + 2] = (float) (vertices[indices[i]]).getZ();
			tvalues[i * 2] = (float) (vertices[indices[i]]).getS();
			tvalues[i * 2 + 1] = (float) (vertices[indices[i]]).getT();
			nvalues[i * 3] = (float) (vertices[indices[i]]).getNormalX();
			nvalues[i * 3 + 1] = (float) (vertices[indices[i]]).getNormalY();
			nvalues[i * 3 + 2] = (float) (vertices[indices[i]]).getNormalZ();
		}

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[40]);
		FloatBuffer vertBuf = FloatBuffer.wrap(fvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertBuf.limit() * 4, vertBuf, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[41]);
		FloatBuffer norBuf = FloatBuffer.wrap(nvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, norBuf.limit() * 4, norBuf, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[42]);
		FloatBuffer texBuf = FloatBuffer.wrap(tvalues);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL.GL_STATIC_DRAW);


	}//40,41,42
	private void setupVertices(GL4 gl) {
		gl.glGenVertexArrays(vao.length, vao,0);
		gl.glBindVertexArray(vao[0]);
		gl.glGenBuffers(vbo.length, vbo, 0);
		setupVerticesSphere(gl);
		setupVerticesRock(gl);
		setupVerticesMyModel(gl);
		setupVerticesGrassModel(gl);
		//setupVerteciesCube(gl);
	}
	// -----------------------------
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{setupShadowBuffers(drawable);}


	private void installLights(int rendering_program, Matrix3D v_matrix, GLAutoDrawable drawable) {
		GL4 gl = (GL4) drawable.getGL();

		Material currentMaterial = new Material();
		currentMaterial = thisMaterial;

		Point3D lightP = currentLight.getPosition();
		Point3D lightPv = lightP.mult(v_matrix);

		float [] currLightPos = new float[] { (float) lightPv.getX(),
				(float) lightPv.getY(),
				(float) lightPv.getZ() };

		// get the location of the global ambient light field in the shader
		int globalAmbLoc = gl.glGetUniformLocation(rendering_program, "globalAmbient");

		// set the current globalAmbient settings
		gl.glProgramUniform4fv(rendering_program, globalAmbLoc, 1, globalAmbient, 0);

		// get the locations of the light and material fields in the shader
		int ambLoc = gl.glGetUniformLocation(rendering_program, "light.ambient");
		int diffLoc = gl.glGetUniformLocation(rendering_program, "light.diffuse");
		int specLoc = gl.glGetUniformLocation(rendering_program, "light.specular");
		int posLoc = gl.glGetUniformLocation(rendering_program, "light.position");

		int MambLoc = gl.glGetUniformLocation(rendering_program, "material.ambient");
		int MdiffLoc = gl.glGetUniformLocation(rendering_program, "material.diffuse");
		int MspecLoc = gl.glGetUniformLocation(rendering_program, "material.specular");
		int MshiLoc = gl.glGetUniformLocation(rendering_program, "material.shininess");

		// set the uniform light and material values in the shader
		gl.glProgramUniform4fv(rendering_program, ambLoc, 1, currentLight.getAmbient(), 0);
		gl.glProgramUniform4fv(rendering_program, diffLoc, 1, currentLight.getDiffuse(), 0);
		gl.glProgramUniform4fv(rendering_program, specLoc, 1, currentLight.getSpecular(), 0);
		gl.glProgramUniform3fv(rendering_program, posLoc, 1, currLightPos, 0);

		gl.glProgramUniform4fv(rendering_program, MambLoc, 1, currentMaterial.getAmbient(), 0);
		gl.glProgramUniform4fv(rendering_program, MdiffLoc, 1, currentMaterial.getDiffuse(), 0);
		gl.glProgramUniform4fv(rendering_program, MspecLoc, 1, currentMaterial.getSpecular(), 0);
		gl.glProgramUniform1f(rendering_program, MshiLoc, currentMaterial.getShininess());
	}

	@Override
	public void dispose(GLAutoDrawable drawable)
	{	GL4 gl = (GL4) drawable.getGL();
		gl.glDeleteVertexArrays(1, vbo, 0);
	}

	//-----------------

	private Matrix3D perspective(float fovy, float aspect, float n, float f) {
		float q = 1.0f / ((float) Math.tan(Math.toRadians(0.5f * fovy)));
		float A = q / aspect;
		float B = (n + f) / (n - f);
		float C = (2.0f * n * f) / (n - f);
		Matrix3D r = new Matrix3D();
		r.setElementAt(0,0,A);
		r.setElementAt(1,1,q);
		r.setElementAt(2,2,B);
		r.setElementAt(3,2,-1.0f);
		r.setElementAt(2,3,C);
		r.setElementAt(3,3,0.0f);
		return r;
	}
	private Matrix3D lookAt(graphicslib3D.Point3D eyeP, graphicslib3D.Point3D centerP, Vector3D upV) {
		Vector3D eyeV = new Vector3D(eyeP);
		Vector3D cenV = new Vector3D(centerP);
		Vector3D f = (cenV.minus(eyeV)).normalize();
		Vector3D sV = (f.cross(upV)).normalize();
		Vector3D nU = (sV.cross(f)).normalize();

		Matrix3D l = new Matrix3D();
		l.setElementAt(0,0,sV.getX());l.setElementAt(0,1,nU.getX());l.setElementAt(0,2,-f.getX());l.setElementAt(0,3,0.0f);
		l.setElementAt(1,0,sV.getY());l.setElementAt(1,1,nU.getY());l.setElementAt(1,2,-f.getY());l.setElementAt(1,3,0.0f);
		l.setElementAt(2,0,sV.getZ());l.setElementAt(2,1,nU.getZ());l.setElementAt(2,2,-f.getZ());l.setElementAt(2,3,0.0f);
		l.setElementAt(3,0,sV.dot(eyeV.mult(-1)));
		l.setElementAt(3,1,nU.dot(eyeV.mult(-1)));
		l.setElementAt(3,2,(f.mult(-1)).dot(eyeV.mult(-1)));
		l.setElementAt(3,3,1.0f);
		return(l.transpose());
	}
	// ------------------------------------------------------------------------------------- CONTROLS
	@Override public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getUnitsToScroll() < 0)
			lightLoc.setY(lightLoc.getY()+.1);
		else
			lightLoc.setY(lightLoc.getY()-.1);
	}
	@Override public void mouseDragged(MouseEvent e) {
		if (mousePoint.getX() > e.getX()) lightLoc.setX(lightLoc.getX()-.1);
		if (mousePoint.getX() < e.getX()) lightLoc.setX(lightLoc.getX()+.1);
		if (mousePoint.getY() > e.getY()) lightLoc.setZ(lightLoc.getZ()-.1);
		if (mousePoint.getY() < e.getY()) lightLoc.setZ(lightLoc.getZ()+.1);
		mousePoint.setLocation(e.getPoint());
	}
	@Override public void mouseMoved(MouseEvent e) {}
	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) { mousePoint.setLocation(e.getPoint());}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void keyTyped(KeyEvent e) {}
	@Override public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_L: {if (lights==0)lights = 1; else lights=0;break;}
			case KeyEvent.VK_SPACE: {axis = !axis;break;}
		}
	}
	@Override public void keyReleased(KeyEvent e) {}
	@Override public void actionPerformed(ActionEvent e) {}
	private void keyMaping() {
		int mapName = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap imap = this.getRootPane().getInputMap(mapName);
		ActionMap amap = this.getRootPane().getActionMap();

		KeyStroke wKey = KeyStroke.getKeyStroke('w');
		imap.put(wKey, "zoomin");
		ZoomIn zoomin = new ZoomIn();
		amap.put("zoomin", zoomin);

		KeyStroke sKey = KeyStroke.getKeyStroke('s');
		imap.put(sKey, "zoomout");
		ZoomOut zoomout = new ZoomOut();
		amap.put("zoomout", zoomout);

		KeyStroke dKey = KeyStroke.getKeyStroke('d');
		imap.put(dKey, "straferight");
		StrafeRight straferight = new StrafeRight();
		amap.put("straferight", straferight);

		KeyStroke aKey = KeyStroke.getKeyStroke('a');
		imap.put(aKey, "strafeleft");
		StrafeLeft strafeleft = new StrafeLeft();
		amap.put("strafeleft", strafeleft);

		KeyStroke eKey = KeyStroke.getKeyStroke('e');
		imap.put(eKey, "upkey");
		up up = new up();
		amap.put("upkey", up);

		KeyStroke qKey = KeyStroke.getKeyStroke('q');
		imap.put(qKey, "downkey");
		down down = new down();
		amap.put("downkey", down);

		KeyStroke upKey = KeyStroke.getKeyStroke("UP");
		imap.put(upKey, "pitchup");
		pitchUp pitchup = new pitchUp();
		amap.put("pitchup", pitchup);

		KeyStroke downKey = KeyStroke.getKeyStroke("DOWN");
		imap.put(downKey, "pitchdown");
		pitchDown pitchdown = new pitchDown();
		amap.put("pitchdown", pitchdown);

		KeyStroke rightKey = KeyStroke.getKeyStroke("RIGHT");
		imap.put(rightKey, "panright");
		panRight panright = new panRight();
		amap.put("panright", panright);

		KeyStroke leftKey = KeyStroke.getKeyStroke("LEFT");
		imap.put(leftKey, "panleft");
		panLeft panleft = new panLeft();
		amap.put("panleft", panleft);

	}
	private class ZoomIn extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			Vector3D t = new Vector3D();
			t.setX(n.getX());t.setY(n.getY());t.setZ(n.getZ());
			t.scale(-0.5f);
			xyz = xyz.add(t);
			zoom += 1f;
		}
	}
	private class ZoomOut extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Vector3D t = new Vector3D();
			t.setX(n.getX());t.setY(n.getY());t.setZ(n.getZ());
			t.scale(0.5f);
			xyz = xyz.add(t);
			//if  (zoom > 1 )
			zoom -= 1f;
		}
	}
	private class StrafeRight extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Vector3D t = new Vector3D();
			t.setX(u.getX());t.setY(u.getY());t.setZ(u.getZ());
			t.scale(0.5f);
			xyz = xyz.add(t);
			strafe += 5f;
			//System.out.println("zoom - 1.0");
		}
	}
	private class StrafeLeft extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Vector3D t = new Vector3D();
			t.setX(u.getX());t.setY(u.getY());t.setZ(u.getZ());
			t.scale(-0.5f);
			xyz = xyz.add(t);
			strafe -= 5f;
			//System.out.println("zoom - 1.0");
		}
	}
	private class down extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Vector3D t = new Vector3D();
			t.setX(v.getX());t.setY(v.getY());t.setZ(v.getZ());
			t.scale(-0.5f);
			xyz = xyz.add(t);
			upDown -= 5f;
		}
	}
	private class up extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Vector3D t = new Vector3D();
			t.setX(v.getX());t.setY(v.getY());t.setZ(v.getZ());
			t.scale(0.5f);
			xyz = xyz.add(t);
			upDown += 5f;

		}
	}
	private class panRight extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Matrix3D r = new Matrix3D();
			r.rotate(-10, v.normalize());
			n = n.mult(r);
			u = u.mult(r);

		}
	}
	private class panLeft extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Matrix3D r = new Matrix3D();
			r.rotate(+10, v.normalize());
			n = n.mult(r);
			u = u.mult(r);

		}
	}
	private class pitchUp extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Matrix3D r = new Matrix3D();
			r.rotate(10, u.normalize());
			n = n.mult(r);
			v = v.mult(r);
		}
	}
	private class pitchDown extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Matrix3D r = new Matrix3D();
			r.rotate(-10, u.normalize());
			n = n.mult(r);
			v = v.mult(r);
		}
	}
}