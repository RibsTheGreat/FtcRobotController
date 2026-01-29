package OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;


import Components.Toggle;
import Components.Drive;
import Components.Indexer;
import Util.Vector2;

@TeleOp(name="MainIFwd", group="TeleOp")
public class MainIFwd extends OpMode {
    Drive drive;
    DcMotorEx frontLeftMotor;
    DcMotorEx backLeftMotor;
    DcMotorEx frontRightMotor;
    DcMotorEx backRightMotor;
    DcMotorEx beltMotor;
    DcMotorEx shooterMotorOne;
    DcMotorEx shooterMotorTwo;
    DcMotorEx liftMotor;
    IMU imu;
    Toggle shooterToggle;
    Toggle beltToggle;

    private final int SHOOTER_VELOCITY = 750;

    private boolean lastDpad_UpState;
    private boolean lastDpad_DownState;

    @Override
    public void init() {
        imu = hardwareMap.get(IMU.class, "imu");
        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "backLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "backRightMotor");

        beltMotor = hardwareMap.get(DcMotorEx.class, "beltMotor");
        shooterMotorOne = hardwareMap.get(DcMotorEx.class, "shooterMotorOne");
        shooterMotorTwo = hardwareMap.get(DcMotorEx.class, "shooterMotorTwo");

        liftMotor = hardwareMap.get(DcMotorEx.class, "liftMotor");

        drive = new Drive(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor, imu);

        backRightMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        shooterMotorOne.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooterMotorTwo.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        backRightMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        frontLeftMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotorOne.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        shooterMotorTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterMotorOne.setDirection(DcMotorEx.Direction.REVERSE);
        shooterMotorTwo.setDirection(DcMotorSimple.Direction.FORWARD);

        double p1 = 1;
        double i1 = 0.0;
        double d1 = 0.0;
        double f1 = 13;

        double p2 = 1;
        double i2 = 0.0;
        double d2 = 0.5;
        double f2 = 13;

        shooterMotorOne.setVelocityPIDFCoefficients(p1, i1, d1, f1);
        shooterMotorTwo.setVelocityPIDFCoefficients(p2, i2, d2, f2);


        //intakeToggle = new Toggle(false);
        shooterToggle = new Toggle(false);
        beltToggle = new Toggle(false);



        telemetry.addData("Status:", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
            telemetry.addData("Status", "Running");


                Vector2 driveDirection = new Vector2(gamepad1.left_stick_x, gamepad1.left_stick_y);
                float driveRotation = gamepad1.right_stick_x;
                drive.moveInDirection(driveDirection, driveRotation, 1.0f, telemetry);

            telemetry.addData("Target Ticks", SHOOTER_VELOCITY);

            shooterToggle.update(gamepad1.b);
            beltToggle.update(gamepad1.y);


            telemetry.addData("Shooting toggle", shooterToggle.getState());
            telemetry.addData("Shooter Motor 1 Ticks", shooterMotorOne.getVelocity());
            telemetry.addData("Shooter Motor 2 Ticks", shooterMotorTwo.getVelocity());


            if(gamepad1.y){
                if (shooterMotorOne.getVelocity() >= 730 && shooterMotorOne.getVelocity() <= 770 && shooterMotorTwo.getVelocity() >= 730 && shooterMotorTwo.getVelocity() <= 770) {
                    beltMotor.setPower(0.35);
                }
                else {
                beltMotor.setPower(0);
                }
            }
            else if(gamepad1.a){
                shooterMotorOne.setVelocity(-800);
                shooterMotorTwo.setVelocity(-800);
                beltMotor.setPower(-.3);
            }
            else {
                beltMotor.setPower(0);
            }

            if (shooterToggle.getState()){
                shooterMotorOne.setVelocity(SHOOTER_VELOCITY);
                shooterMotorTwo.setVelocity(SHOOTER_VELOCITY);
            }

            if(gamepad1.dpad_up){
                liftMotor.setPower(1);
            }
            else if (gamepad1.dpad_down) {
                liftMotor.setPower(-1);
            }
            else{
                liftMotor.setPower(0);
            }

        if(!shooterToggle.getState()){
                shooterMotorOne.setPower(0);
                shooterMotorTwo.setPower(0);
            }





            telemetry.update();



    }
}
