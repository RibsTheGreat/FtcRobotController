double voltage = getBatteryVoltage();
    voltage = Math.max(11.0, Math.min(voltage, 13.5));

    double compensatedF = 13.5 * (12.0 / voltage);

    shooterMotorOne.setVelocityPIDFCoefficients(p1, i1, d1, compensatedF);
    shooterMotorTwo.setVelocityPIDFCoefficients(p1, i1, d1, compensatedF);

    telemetry.addData("Battery V", voltage);
    telemetry.addData("Shooter F", compensatedF);