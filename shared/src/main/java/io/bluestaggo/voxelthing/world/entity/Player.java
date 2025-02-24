package io.bluestaggo.voxelthing.world.entity;

import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.world.World;
import org.joml.Vector2d;

public class Player extends Entity {
	private final IPlayerController controller;

	private double prevWalkAmount;
	private double walkAmount;
	private double prevRenderWalk;
	private double renderWalk;
	private double prevWalkDir;
	private double walkDir;

	public double accel = 0.2;
	public double friction = 0.6;
	public double jumpHeight = 0.5;

	public Player(World world, IPlayerController controller) {
		super(world);
		this.controller = controller;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public void onGameUpdate() {
		rotYaw += controller.moveYaw();
		rotPitch += controller.movePitch();
		rotPitch = MathUtil.clamp(rotPitch, -89.0f, 89.0f);
	}

	protected void update() {
		super.update();

		double yawRad = Math.toRadians(rotYaw);
		double forward = controller.moveForward();
		double side = controller.moveSide();

		double dist = Math.sqrt(forward * forward + side * side);
		if (dist > 0.0) {
			forward /= dist;
			side /= dist;
		}
		forward *= accel;
		side *= accel;

		velX += forward * Math.cos(yawRad) + side * Math.cos(yawRad + Math.PI / 2.0);
		velZ += forward * Math.sin(yawRad) + side * Math.sin(yawRad + Math.PI / 2.0);
		velX *= friction;
		velZ *= friction;

		double walkAdd = Math.min(Math.sqrt(velX * velX + velZ * velZ), 1.0);
		if (!onGround) {
			walkAdd /= 2.5;
		}

		prevWalkAmount = walkAmount;
		walkAmount += walkAdd * 1.5;
		prevRenderWalk = renderWalk;
		renderWalk = Math.sin(walkAmount) * Math.min(walkAdd * 10.0, 1.0);
		prevWalkDir = walkDir;
		if (walkAdd > 0.1) {
			walkDir = Math.toDegrees(new Vector2d(velX, velZ).angle(new Vector2d(-1.0f, 0.0f)));
		}

		if (onGround && walkAmount - prevWalkAmount < 0.01) {
			walkAmount = 0.0;
		}

		if (onGround && controller.doJump()) {
			velY = jumpHeight;
		}
	}

	public double getRenderWalk() {
		return world.scaleToTick(prevRenderWalk, renderWalk);
	}

	public float getRenderWalkDir() {
		return (float) world.scaleToTick(prevWalkDir, walkDir);
	}

	@Override
	public float getRenderY() {
		return super.getRenderY() + (float) Math.abs(getRenderWalk() / 3.0);
	}

	@Override
	public int getRenderFrame() {
		int frame = 1;
		double walk = getRenderWalk();

		if (walk >= 0.5) {
			frame++;
		} else if (walk <= -0.5) {
			frame--;
		}

		return frame;
	}

	@Override
	public int getRenderRotation(float cameraAngle) {
		return (int) (MathUtil.floorMod(getRenderWalkDir() + cameraAngle + 22.5f, 360.0f) / 45.0f) % 8;
	}
}
