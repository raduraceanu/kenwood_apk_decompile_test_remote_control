package com.jvckenwood.carconnectcontrol.lib.api.info;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class VCanInfo implements Parcelable, Cloneable {
    public static final Parcelable.Creator<VCanInfo> CREATOR = new Parcelable.Creator<VCanInfo>() { // from class: com.jvckenwood.carconnectcontrol.lib.api.info.VCanInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VCanInfo createFromParcel(Parcel p) {
            VCanInfo o = new VCanInfo();
            o.updateTime = new Date(p.readLong());
            o.updateFlag1 = p.readLong();
            o.updateFlag2 = p.readLong();
            o.updateFlag3 = p.readLong();
            o.updateFlag4 = p.readLong();
            o.vehicleSpeed = p.readInt();
            o.yawRate = p.readInt();
            o.steeringAngle = p.readInt();
            o.acceleratorPedalPosition1 = p.readInt();
            o.acceleratorPedalPosition2 = p.readInt();
            o.brakeSW = p.readInt();
            o.longitudinalG = p.readInt();
            o.lateralG = p.readInt();
            o.engineSpeed = p.readInt();
            o.engineCoolantTemperature = p.readInt();
            o.turningIndicatorLamp = p.readInt();
            o.ecoStatus = p.readInt();
            o.headway = p.readInt();
            o.averageInjectionFuel = p.readInt();
            o.fuelConsumption = p.readInt();
            o.moduleTemperature1 = p.readInt();
            o.moduleTemperature2 = p.readInt();
            o.moduleTemperature3 = p.readInt();
            o.moduleTemperature4 = p.readInt();
            o.moduleTemperature5 = p.readInt();
            o.moduleTemperature6 = p.readInt();
            o.batteryCellMaximumTemperature = p.readInt();
            o.batteryCellMinimumTemperature = p.readInt();
            o.stateOfCharge = p.readInt();
            o.batteryCurrent = p.readInt();
            o.batteryVoltage = p.readInt();
            o.airConditionerPower = p.readInt();
            o.heaterPower = p.readInt();
            o.shiftPosition = p.readInt();
            o.currentGearPosition = p.readInt();
            o.prndlDisplayRequest = p.readInt();
            o.acSwStatus = p.readInt();
            o.acTempDialSw = p.readInt();
            o.blowerFanOutput = p.readInt();
            o.acModeStatus = p.readInt();
            o.freshRecirculationModeStatus = p.readInt();
            o.cabinTemp = p.readInt();
            o.ambienceTemp = p.readInt();
            o.vehicleIdentificationNumber = p.readLong();
            o.modelYear = p.readInt();
            o.vehicleLine = p.readInt();
            o.fuelLevel = p.readInt();
            o.headLampStatus = p.readInt();
            o.roomLampStatus = p.readInt();
            o.wiperSwInfomation = p.readInt();
            o.averageFuelConsumption = p.readInt();
            o.distanceToEmptyFuel = p.readInt();
            o.parkingSw = p.readInt();
            o.doorStatus = p.readInt();
            o.driverDoorAjar = p.readInt();
            o.passengerDoorAjar = p.readInt();
            o.rightrearDoorAjar = p.readInt();
            o.leftrearDoorAjar = p.readInt();
            o.trunkLiftgateAjar = p.readInt();
            o.flipperGlassAjar = p.readInt();
            o.odo = p.readInt();
            o.absBrakeEvent = p.readInt();
            o.anyImpactEvent = p.readInt();
            o.ignitionSw = p.readInt();
            o.accSw = p.readInt();
            o.lowWasherFluidWarning = p.readInt();
            o.lowBrakeFluidWarning = p.readInt();
            o.tirePressureIndicationStatus = p.readInt();
            o.smartEntryErrorCode = p.readInt();
            o.parkBrakeEngaged = p.readInt();
            o.overheating = p.readInt();
            o.chargingSystemFailure = p.readInt();
            o.ascIndicatorLamp = p.readInt();
            o.absFaultyIndicatorLamp = p.readInt();
            o.milOn = p.readInt();
            return o;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VCanInfo[] newArray(int size) {
            return new VCanInfo[size];
        }
    };
    public static final long FLAG_CAN1_ACCELERATOR_PEDAL_POSITION1 = 8;
    public static final long FLAG_CAN1_ACCELERATOR_PEDAL_POSITION2 = 16;
    public static final long FLAG_CAN1_AVERAGE_INJECTION_FUEL = 8192;
    public static final long FLAG_CAN1_BATTERY_CELL_MAXIMUM_TEMPERATURE = 2097152;
    public static final long FLAG_CAN1_BATTERY_CELL_MINIMUM_TEMPERATURE = 4194304;
    public static final long FLAG_CAN1_BRAKE_SW = 32;
    public static final long FLAG_CAN1_ECO_STATUS = 128;
    public static final long FLAG_CAN1_ENGINE_COOLANT_TEMPERATURE = 2048;
    public static final long FLAG_CAN1_ENGINE_SPEED = 1024;
    public static final long FLAG_CAN1_FUEL_CONSUMPTION = 16384;
    public static final long FLAG_CAN1_HEADWAY = 4096;
    public static final long FLAG_CAN1_LATERAL_G = 512;
    public static final long FLAG_CAN1_LONGITUDINAL_G = 256;
    public static final long FLAG_CAN1_MODULE_TEMPERATURE1 = 32768;
    public static final long FLAG_CAN1_MODULE_TEMPERATURE2 = 65536;
    public static final long FLAG_CAN1_MODULE_TEMPERATURE3 = 131072;
    public static final long FLAG_CAN1_MODULE_TEMPERATURE4 = 262144;
    public static final long FLAG_CAN1_MODULE_TEMPERATURE5 = 524288;
    public static final long FLAG_CAN1_MODULE_TEMPERATURE6 = 1048576;
    public static final long FLAG_CAN1_STEERING_ANGLE = 4;
    public static final long FLAG_CAN1_TURNING_INDICATOR_LAMP = 64;
    public static final long FLAG_CAN1_VEHICLE_SPEED = 1;
    public static final long FLAG_CAN1_YAW_RATE = 2;
    public static final long FLAG_CAN2_AC_MODE_STATUS = 4096;
    public static final long FLAG_CAN2_AC_SW_STATUS = 128;
    public static final long FLAG_CAN2_AC_TEMP_DIAL_SW = 1024;
    public static final long FLAG_CAN2_AIR_CONDITIONER_POWER = 8;
    public static final long FLAG_CAN2_AMBIENCE_TEMP = 16384;
    public static final long FLAG_CAN2_BATTERY_CURRENT = 2;
    public static final long FLAG_CAN2_BATTERY_VOLTAGE = 4;
    public static final long FLAG_CAN2_BLOWER_FAN_OUTPUT = 2048;
    public static final long FLAG_CAN2_CABIN_TEMP = 8192;
    public static final long FLAG_CAN2_CURRENT_GEAR_POSITION = 64;
    public static final long FLAG_CAN2_FRESH_RECIRCULATION_MODE_STATUS = 256;
    public static final long FLAG_CAN2_HEATER_POWER = 16;
    public static final long FLAG_CAN2_MODEL_YEAR = 65536;
    public static final long FLAG_CAN2_PRNDL_DISPLAY_REQUEST = 512;
    public static final long FLAG_CAN2_SHIFT_POSITION = 32;
    public static final long FLAG_CAN2_STATE_OF_CHARGE = 1;
    public static final long FLAG_CAN2_VEHICLE_IDENTIFICATION_NUMBER = 32768;
    public static final long FLAG_CAN2_VEHICLE_LINE = 131072;
    public static final long FLAG_CAN3_AVERAGE_FUEL_CONSUMPTION = 32;
    public static final long FLAG_CAN3_DISTANCE_TO_EMPTY_FUEL = 64;
    public static final long FLAG_CAN3_DOOR_STATUS = 128;
    public static final long FLAG_CAN3_DRIVER_DOOR_AJAR = 256;
    public static final long FLAG_CAN3_FLIPPER_GLASS_AJAR = 8192;
    public static final long FLAG_CAN3_FUEL_LEVEL = 1;
    public static final long FLAG_CAN3_HEAD_LAMP_STATUS = 2;
    public static final long FLAG_CAN3_LEFT_REAR_DOOR_AJAR = 2048;
    public static final long FLAG_CAN3_PARKING_SW = 16;
    public static final long FLAG_CAN3_PASSENGER_DOOR_AJAR = 512;
    public static final long FLAG_CAN3_RIGHTREAR_DOOR_AJAR = 1024;
    public static final long FLAG_CAN3_ROOM_LAMP_STATUS = 4;
    public static final long FLAG_CAN3_TRUNK_LIFTGATE_AJAR = 4096;
    public static final long FLAG_CAN3_WIPER_SW_INFOMATION = 8;
    public static final long FLAG_CAN4_ABS_BRAKE_EVENT = 2;
    public static final long FLAG_CAN4_ABS_FAULTY_INDICATOR_LAMP = 8192;
    public static final long FLAG_CAN4_ACC_SW = 16;
    public static final long FLAG_CAN4_ANY_IMPACT_EVENT = 4;
    public static final long FLAG_CAN4_ASC_INDICATOR_LAMP = 4096;
    public static final long FLAG_CAN4_CHARGING_SYSTEM_FAILURE = 2048;
    public static final long FLAG_CAN4_IGNITION_SW = 8;
    public static final long FLAG_CAN4_LOW_BRAKE_FLUID_WARNING = 64;
    public static final long FLAG_CAN4_LOW_WASHER_FLUID_WARNING = 32;
    public static final long FLAG_CAN4_MIL_ON = 16384;
    public static final long FLAG_CAN4_ODO = 1;
    public static final long FLAG_CAN4_OVERHEATING = 1024;
    public static final long FLAG_CAN4_PARK_BRAKE_ENGAGED = 512;
    public static final long FLAG_CAN4_SMART_ENTRY_ERROR_CODE = 256;
    public static final long FLAG_CAN4_TIRE_PRESSURE_INDICATION_STATUS = 128;
    private int absBrakeEvent;
    private int absFaultyIndicatorLamp;
    private int acModeStatus;
    private int acSwStatus;
    private int acTempDialSw;
    private int accSw;
    private int acceleratorPedalPosition1;
    private int acceleratorPedalPosition2;
    private int airConditionerPower;
    private int ambienceTemp;
    private int anyImpactEvent;
    private int ascIndicatorLamp;
    private int averageFuelConsumption;
    private int averageInjectionFuel;
    private int batteryCellMaximumTemperature;
    private int batteryCellMinimumTemperature;
    private int batteryCurrent;
    private int batteryVoltage;
    private int blowerFanOutput;
    private int brakeSW;
    private int cabinTemp;
    private int chargingSystemFailure;
    private int currentGearPosition;
    private int distanceToEmptyFuel;
    private int doorStatus;
    private int driverDoorAjar;
    private int ecoStatus;
    private int engineCoolantTemperature;
    private int engineSpeed;
    private int flipperGlassAjar;
    private int freshRecirculationModeStatus;
    private int fuelConsumption;
    private int fuelLevel;
    private int headLampStatus;
    private int headway;
    private int heaterPower;
    private int ignitionSw;
    private int lateralG;
    private int leftrearDoorAjar;
    private int longitudinalG;
    private int lowBrakeFluidWarning;
    private int lowWasherFluidWarning;
    private int milOn;
    private int modelYear;
    private int moduleTemperature1;
    private int moduleTemperature2;
    private int moduleTemperature3;
    private int moduleTemperature4;
    private int moduleTemperature5;
    private int moduleTemperature6;
    private int odo;
    private int overheating;
    private int parkBrakeEngaged;
    private int parkingSw;
    private int passengerDoorAjar;
    private int prndlDisplayRequest;
    private int rightrearDoorAjar;
    private int roomLampStatus;
    private int shiftPosition;
    private int smartEntryErrorCode;
    private int stateOfCharge;
    private int steeringAngle;
    private int tirePressureIndicationStatus;
    private int trunkLiftgateAjar;
    private int turningIndicatorLamp;
    private long updateFlag1;
    private long updateFlag2;
    private long updateFlag3;
    private long updateFlag4;
    private Date updateTime;
    private long vehicleIdentificationNumber;
    private int vehicleLine;
    private int vehicleSpeed;
    private int wiperSwInfomation;
    private int yawRate;

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime() {
        this.updateTime = new Date();
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = new Date(updateTime);
    }

    public long getUpdateFlag1() {
        return this.updateFlag1;
    }

    public void setUpdateFlag1(long updateFlag) {
        this.updateFlag1 = updateFlag;
    }

    public long getUpdateFlag2() {
        return this.updateFlag2;
    }

    public void setUpdateFlag2(long updateFlag) {
        this.updateFlag2 = updateFlag;
    }

    public long getUpdateFlag3() {
        return this.updateFlag3;
    }

    public void setUpdateFlag3(long updateFlag) {
        this.updateFlag3 = updateFlag;
    }

    public long getUpdateFlag4() {
        return this.updateFlag4;
    }

    public void setUpdateFlag4(long updateFlag) {
        this.updateFlag4 = updateFlag;
    }

    public int getVehicleSpeed() {
        return this.vehicleSpeed;
    }

    public void setVehicleSpeed(int speed) {
        this.vehicleSpeed = speed;
    }

    public int getYawRate() {
        return this.yawRate;
    }

    public void setYawRate(int yawRate) {
        this.yawRate = yawRate;
    }

    public int getSteeringAngle() {
        return this.steeringAngle;
    }

    public void setSteeringAngle(int steeringAngle) {
        this.steeringAngle = steeringAngle;
    }

    public int getAcceleratorPedalPosition1() {
        return this.acceleratorPedalPosition1;
    }

    public void setAcceleratorPedalPosition1(int acceleratorPedalPosition) {
        this.acceleratorPedalPosition1 = acceleratorPedalPosition;
    }

    public int getAcceleratorPedalPosition2() {
        return this.acceleratorPedalPosition2;
    }

    public void setAcceleratorPedalPosition2(int acceleratorPedalPosition) {
        this.acceleratorPedalPosition2 = acceleratorPedalPosition;
    }

    public int getBrakeSW() {
        return this.brakeSW;
    }

    public void setBrakeSW(int brakeSW) {
        this.brakeSW = brakeSW;
    }

    public int getLongitudinalG() {
        return this.longitudinalG;
    }

    public void setLongitudinalG(int longitudinalG) {
        this.longitudinalG = longitudinalG;
    }

    public int getLateralG() {
        return this.lateralG;
    }

    public void setLateralG(int lateralG) {
        this.lateralG = lateralG;
    }

    public int getEngineSpeed() {
        return this.engineSpeed;
    }

    public void setEngineSpeed(int engineSpeed) {
        this.engineSpeed = engineSpeed;
    }

    public int getEngineCoolantTemperature() {
        return this.engineCoolantTemperature;
    }

    public void setEngineCoolantTemperature(int engineCoolantTemperature) {
        this.engineCoolantTemperature = engineCoolantTemperature;
    }

    public int getTurningIndicatorLamp() {
        return this.turningIndicatorLamp;
    }

    public void setTurningIndicatorLamp(int turningIndicatorLamp) {
        this.turningIndicatorLamp = turningIndicatorLamp;
    }

    public int getEcoStatus() {
        return this.ecoStatus;
    }

    public void setEcoStatus(int ecoStatus) {
        this.ecoStatus = ecoStatus;
    }

    public int getHeadway() {
        return this.headway;
    }

    public void setHeadway(int headway) {
        this.headway = headway;
    }

    public int getAverageInjectionFuel() {
        return this.averageInjectionFuel;
    }

    public void setAverageInjectionFuel(int averageInjectionFuel) {
        this.averageInjectionFuel = averageInjectionFuel;
    }

    public int getFuelConsumption() {
        return this.fuelConsumption;
    }

    public void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public int getModuleTemperature1() {
        return this.moduleTemperature1;
    }

    public void setModuleTemperature1(int moduleTemperature1) {
        this.moduleTemperature1 = moduleTemperature1;
    }

    public int getModuleTemperature2() {
        return this.moduleTemperature2;
    }

    public void setModuleTemperature2(int moduleTemperature2) {
        this.moduleTemperature2 = moduleTemperature2;
    }

    public int getModuleTemperature3() {
        return this.moduleTemperature3;
    }

    public void setModuleTemperature3(int moduleTemperature3) {
        this.moduleTemperature3 = moduleTemperature3;
    }

    public int getModuleTemperature4() {
        return this.moduleTemperature4;
    }

    public void setModuleTemperature4(int moduleTemperature4) {
        this.moduleTemperature4 = moduleTemperature4;
    }

    public int getModuleTemperature5() {
        return this.moduleTemperature5;
    }

    public void setModuleTemperature5(int moduleTemperature5) {
        this.moduleTemperature5 = moduleTemperature5;
    }

    public int getModuleTemperature6() {
        return this.moduleTemperature6;
    }

    public void setModuleTemperature6(int moduleTemperature6) {
        this.moduleTemperature6 = moduleTemperature6;
    }

    public int getBatteryCellMaximumTemperature() {
        return this.batteryCellMaximumTemperature;
    }

    public void setBatteryCellMaximumTemperature(int batteryCellMaximumTemperature) {
        this.batteryCellMaximumTemperature = batteryCellMaximumTemperature;
    }

    public int getBatteryCellMinimumTemperature() {
        return this.batteryCellMinimumTemperature;
    }

    public void setBatteryCellMinimumTemperature(int batteryCellMinimumTemperature) {
        this.batteryCellMinimumTemperature = batteryCellMinimumTemperature;
    }

    public int getStateOfCharge() {
        return this.stateOfCharge;
    }

    public void setStateOfCharge(int stateOfCharge) {
        this.stateOfCharge = stateOfCharge;
    }

    public int getBatteryCurrent() {
        return this.batteryCurrent;
    }

    public void setBatteryCurrent(int batteryCurrent) {
        this.batteryCurrent = batteryCurrent;
    }

    public int getBatteryVoltage() {
        return this.batteryVoltage;
    }

    public void setBatteryVoltage(int batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public int getAirConditionerPower() {
        return this.airConditionerPower;
    }

    public void setAirConditionerPower(int airConditionerPower) {
        this.airConditionerPower = airConditionerPower;
    }

    public int getHeaterPower() {
        return this.heaterPower;
    }

    public void setHeaterPower(int heaterPower) {
        this.heaterPower = heaterPower;
    }

    public int getShiftPosition() {
        return this.shiftPosition;
    }

    public void setShiftPosition(int shiftPosition) {
        this.shiftPosition = shiftPosition;
    }

    public int getCurrentGearPosition() {
        return this.currentGearPosition;
    }

    public void setCurrentGearPosition(int currentGearPosition) {
        this.currentGearPosition = currentGearPosition;
    }

    public int getPrndlDisplayRequest() {
        return this.prndlDisplayRequest;
    }

    public void setPrndlDisplayRequest(int prndlDisplayRequest) {
        this.prndlDisplayRequest = prndlDisplayRequest;
    }

    public int getAcSwStatus() {
        return this.acSwStatus;
    }

    public void setAcSwStatus(int acSwStatus) {
        this.acSwStatus = acSwStatus;
    }

    public int getAcTempDialSw() {
        return this.acTempDialSw;
    }

    public void setAcTempDialSw(int acTempDialSw) {
        this.acTempDialSw = acTempDialSw;
    }

    public int getBlowerFanOutput() {
        return this.blowerFanOutput;
    }

    public void setBlowerFanOutput(int blowerFanOutput) {
        this.blowerFanOutput = blowerFanOutput;
    }

    public int getAcModeStatus() {
        return this.acModeStatus;
    }

    public void setAcModeStatus(int acModeStatus) {
        this.acModeStatus = acModeStatus;
    }

    public int getFreshRecirculationModeStatus() {
        return this.freshRecirculationModeStatus;
    }

    public void setFreshRecirculationModeStatus(int freshRecirculationModeStatus) {
        this.freshRecirculationModeStatus = freshRecirculationModeStatus;
    }

    public int getCabinTemp() {
        return this.cabinTemp;
    }

    public void setCabinTemp(int cabinTemp) {
        this.cabinTemp = cabinTemp;
    }

    public int getAmbienceTemp() {
        return this.ambienceTemp;
    }

    public void setAmbienceTemp(int ambienceTemp) {
        this.ambienceTemp = ambienceTemp;
    }

    public long getVehicleIdentificationNumber() {
        return this.vehicleIdentificationNumber;
    }

    public void setVehicleIdentificationNumber(long vehicleIdentificationNumber) {
        this.vehicleIdentificationNumber = vehicleIdentificationNumber;
    }

    public int getModelYear() {
        return this.modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public int getVehicleLine() {
        return this.vehicleLine;
    }

    public void setVehicleLine(int vehicleLine) {
        this.vehicleLine = vehicleLine;
    }

    public int getFuelLevel() {
        return this.fuelLevel;
    }

    public void setFuelLevel(int fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public int getHeadLampStatus() {
        return this.headLampStatus;
    }

    public void setHeadLampStatus(int headLampStatus) {
        this.headLampStatus = headLampStatus;
    }

    public int getRoomLampStatus() {
        return this.roomLampStatus;
    }

    public void setRoomLampStatus(int roomLampStatus) {
        this.roomLampStatus = roomLampStatus;
    }

    public int getWiperSwInfomation() {
        return this.wiperSwInfomation;
    }

    public void setWiperSwInfomation(int wiperSwInfomation) {
        this.wiperSwInfomation = wiperSwInfomation;
    }

    public int getAverageFuelConsumption() {
        return this.averageFuelConsumption;
    }

    public void setAverageFuelConsumption(int averageFuelConsumption) {
        this.averageFuelConsumption = averageFuelConsumption;
    }

    public int getDistanceToEmptyFuel() {
        return this.distanceToEmptyFuel;
    }

    public void setDistanceToEmptyFuel(int distanceToEmptyFuel) {
        this.distanceToEmptyFuel = distanceToEmptyFuel;
    }

    public int getParkingSw() {
        return this.parkingSw;
    }

    public void setParkingSw(int parkingSw) {
        this.parkingSw = parkingSw;
    }

    public int getDoorStatus() {
        return this.doorStatus;
    }

    public void setDoorStatus(int doorStatus) {
        this.doorStatus = doorStatus;
    }

    public int getDriverDoorAjar() {
        return this.driverDoorAjar;
    }

    public void setDriverDoorAjar(int driverDoorAjar) {
        this.driverDoorAjar = driverDoorAjar;
    }

    public int getPassengerDoorAjar() {
        return this.passengerDoorAjar;
    }

    public void setPassengerDoorAjar(int passengerDoorAjar) {
        this.passengerDoorAjar = passengerDoorAjar;
    }

    public int getRightrearDoorAjar() {
        return this.rightrearDoorAjar;
    }

    public void setRightrearDoorAjar(int rightRearDoorAjar) {
        this.rightrearDoorAjar = rightRearDoorAjar;
    }

    public int getLeftrearDoorAjar() {
        return this.leftrearDoorAjar;
    }

    public void setLeftrearDoorAjar(int leftRearDoorAjar) {
        this.leftrearDoorAjar = leftRearDoorAjar;
    }

    public int getTrunkLiftgateAjar() {
        return this.trunkLiftgateAjar;
    }

    public void setTrunkLiftgateAjar(int trunkLiftgateAjar) {
        this.trunkLiftgateAjar = trunkLiftgateAjar;
    }

    public int getFlipperGlassAjar() {
        return this.flipperGlassAjar;
    }

    public void setFlipperGlassAjar(int flipperGlassAjar) {
        this.flipperGlassAjar = flipperGlassAjar;
    }

    public int getOdo() {
        return this.odo;
    }

    public void setOdo(int odo) {
        this.odo = odo;
    }

    public int getAbsBrakeEvent() {
        return this.absBrakeEvent;
    }

    public void setAbsBrakeEvent(int absBrakeEvent) {
        this.absBrakeEvent = absBrakeEvent;
    }

    public int getAnyImpactEvent() {
        return this.anyImpactEvent;
    }

    public void setAnyImpactEvent(int anyImpactEvent) {
        this.anyImpactEvent = anyImpactEvent;
    }

    public int getIgnitionSw() {
        return this.ignitionSw;
    }

    public void setIgnitionSw(int ignitionSw) {
        this.ignitionSw = ignitionSw;
    }

    public int getAccSw() {
        return this.accSw;
    }

    public void setAccSw(int accSw) {
        this.accSw = accSw;
    }

    public int getLowWasherFluidWarning() {
        return this.lowWasherFluidWarning;
    }

    public void setLowWasherFluidWarning(int lowWasherFluidWarning) {
        this.lowWasherFluidWarning = lowWasherFluidWarning;
    }

    public int getLowBrakeFluidWarning() {
        return this.lowBrakeFluidWarning;
    }

    public void setLowBrakeFluidWarning(int lowBrakeFluidWarning) {
        this.lowBrakeFluidWarning = lowBrakeFluidWarning;
    }

    public int getTirePressureIndicationStatus() {
        return this.tirePressureIndicationStatus;
    }

    public void setTirePressureIndicationStatus(int tirePressureIndicationStatus) {
        this.tirePressureIndicationStatus = tirePressureIndicationStatus;
    }

    public int getSmartEntryErrorCode() {
        return this.smartEntryErrorCode;
    }

    public void setSmartEntryErrorCode(int smartEntryErrorCode) {
        this.smartEntryErrorCode = smartEntryErrorCode;
    }

    public int getParkBrakeEngaged() {
        return this.parkBrakeEngaged;
    }

    public void setParkBrakeEngaged(int parkBrakeEngaged) {
        this.parkBrakeEngaged = parkBrakeEngaged;
    }

    public int getOverheating() {
        return this.overheating;
    }

    public void setOverheating(int overheating) {
        this.overheating = overheating;
    }

    public int getChargingSystemFailure() {
        return this.chargingSystemFailure;
    }

    public void setChargingSystemFailure(int chargingSystemFailure) {
        this.chargingSystemFailure = chargingSystemFailure;
    }

    public int getAscIndicatorLamp() {
        return this.ascIndicatorLamp;
    }

    public void setAscIndicatorLamp(int ascIndicatorLamp) {
        this.ascIndicatorLamp = ascIndicatorLamp;
    }

    public int getAbsFaultyIndicatorLamp() {
        return this.absFaultyIndicatorLamp;
    }

    public void setAbsFaultyIndicatorLamp(int absFaultyIndicatorLamp) {
        this.absFaultyIndicatorLamp = absFaultyIndicatorLamp;
    }

    public int getMilOn() {
        return this.milOn;
    }

    public void setMilOn(int milOn) {
        this.milOn = milOn;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        p.writeLong(this.updateTime.getTime());
        p.writeLong(this.updateFlag1);
        p.writeLong(this.updateFlag2);
        p.writeLong(this.updateFlag3);
        p.writeLong(this.updateFlag4);
        p.writeInt(this.vehicleSpeed);
        p.writeInt(this.yawRate);
        p.writeInt(this.steeringAngle);
        p.writeInt(this.acceleratorPedalPosition1);
        p.writeInt(this.acceleratorPedalPosition2);
        p.writeInt(this.brakeSW);
        p.writeInt(this.longitudinalG);
        p.writeInt(this.lateralG);
        p.writeInt(this.engineSpeed);
        p.writeInt(this.engineCoolantTemperature);
        p.writeInt(this.turningIndicatorLamp);
        p.writeInt(this.ecoStatus);
        p.writeInt(this.headway);
        p.writeInt(this.averageInjectionFuel);
        p.writeInt(this.fuelConsumption);
        p.writeInt(this.moduleTemperature1);
        p.writeInt(this.moduleTemperature2);
        p.writeInt(this.moduleTemperature3);
        p.writeInt(this.moduleTemperature4);
        p.writeInt(this.moduleTemperature5);
        p.writeInt(this.moduleTemperature6);
        p.writeInt(this.batteryCellMaximumTemperature);
        p.writeInt(this.batteryCellMinimumTemperature);
        p.writeInt(this.stateOfCharge);
        p.writeInt(this.batteryCurrent);
        p.writeInt(this.batteryVoltage);
        p.writeInt(this.airConditionerPower);
        p.writeInt(this.heaterPower);
        p.writeInt(this.shiftPosition);
        p.writeInt(this.currentGearPosition);
        p.writeInt(this.prndlDisplayRequest);
        p.writeInt(this.acSwStatus);
        p.writeInt(this.acTempDialSw);
        p.writeInt(this.blowerFanOutput);
        p.writeInt(this.acModeStatus);
        p.writeInt(this.freshRecirculationModeStatus);
        p.writeInt(this.cabinTemp);
        p.writeInt(this.ambienceTemp);
        p.writeLong(this.vehicleIdentificationNumber);
        p.writeInt(this.modelYear);
        p.writeInt(this.vehicleLine);
        p.writeInt(this.fuelLevel);
        p.writeInt(this.headLampStatus);
        p.writeInt(this.roomLampStatus);
        p.writeInt(this.wiperSwInfomation);
        p.writeInt(this.averageFuelConsumption);
        p.writeInt(this.distanceToEmptyFuel);
        p.writeInt(this.parkingSw);
        p.writeInt(this.doorStatus);
        p.writeInt(this.driverDoorAjar);
        p.writeInt(this.passengerDoorAjar);
        p.writeInt(this.rightrearDoorAjar);
        p.writeInt(this.leftrearDoorAjar);
        p.writeInt(this.trunkLiftgateAjar);
        p.writeInt(this.flipperGlassAjar);
        p.writeInt(this.odo);
        p.writeInt(this.absBrakeEvent);
        p.writeInt(this.anyImpactEvent);
        p.writeInt(this.ignitionSw);
        p.writeInt(this.accSw);
        p.writeInt(this.lowWasherFluidWarning);
        p.writeInt(this.lowBrakeFluidWarning);
        p.writeInt(this.tirePressureIndicationStatus);
        p.writeInt(this.smartEntryErrorCode);
        p.writeInt(this.parkBrakeEngaged);
        p.writeInt(this.overheating);
        p.writeInt(this.chargingSystemFailure);
        p.writeInt(this.ascIndicatorLamp);
        p.writeInt(this.absFaultyIndicatorLamp);
        p.writeInt(this.milOn);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public VCanInfo m5clone() {
        VCanInfo o = new VCanInfo();
        o.updateTime = (Date) this.updateTime.clone();
        o.updateFlag1 = this.updateFlag1;
        o.updateFlag2 = this.updateFlag2;
        o.updateFlag3 = this.updateFlag3;
        o.updateFlag4 = this.updateFlag4;
        o.vehicleSpeed = this.vehicleSpeed;
        o.yawRate = this.yawRate;
        o.steeringAngle = this.steeringAngle;
        o.acceleratorPedalPosition1 = this.acceleratorPedalPosition1;
        o.acceleratorPedalPosition2 = this.acceleratorPedalPosition2;
        o.brakeSW = this.brakeSW;
        o.longitudinalG = this.longitudinalG;
        o.lateralG = this.lateralG;
        o.engineSpeed = this.engineSpeed;
        o.engineCoolantTemperature = this.engineCoolantTemperature;
        o.turningIndicatorLamp = this.turningIndicatorLamp;
        o.ecoStatus = this.ecoStatus;
        o.headway = this.headway;
        o.averageInjectionFuel = this.averageInjectionFuel;
        o.fuelConsumption = this.fuelConsumption;
        o.moduleTemperature1 = this.moduleTemperature1;
        o.moduleTemperature2 = this.moduleTemperature2;
        o.moduleTemperature3 = this.moduleTemperature3;
        o.moduleTemperature4 = this.moduleTemperature4;
        o.moduleTemperature5 = this.moduleTemperature5;
        o.moduleTemperature6 = this.moduleTemperature6;
        o.batteryCellMaximumTemperature = this.batteryCellMaximumTemperature;
        o.batteryCellMinimumTemperature = this.batteryCellMinimumTemperature;
        o.stateOfCharge = this.stateOfCharge;
        o.batteryCurrent = this.batteryCurrent;
        o.batteryVoltage = this.batteryVoltage;
        o.airConditionerPower = this.airConditionerPower;
        o.heaterPower = this.heaterPower;
        o.shiftPosition = this.shiftPosition;
        o.currentGearPosition = this.currentGearPosition;
        o.prndlDisplayRequest = this.prndlDisplayRequest;
        o.acSwStatus = this.acSwStatus;
        o.acTempDialSw = this.acTempDialSw;
        o.blowerFanOutput = this.blowerFanOutput;
        o.acModeStatus = this.acModeStatus;
        o.freshRecirculationModeStatus = this.freshRecirculationModeStatus;
        o.cabinTemp = this.cabinTemp;
        o.ambienceTemp = this.ambienceTemp;
        o.vehicleIdentificationNumber = this.vehicleIdentificationNumber;
        o.modelYear = this.modelYear;
        o.vehicleLine = this.vehicleLine;
        o.fuelLevel = this.fuelLevel;
        o.headLampStatus = this.headLampStatus;
        o.roomLampStatus = this.roomLampStatus;
        o.wiperSwInfomation = this.wiperSwInfomation;
        o.averageFuelConsumption = this.averageFuelConsumption;
        o.distanceToEmptyFuel = this.distanceToEmptyFuel;
        o.parkingSw = this.parkingSw;
        o.doorStatus = this.doorStatus;
        o.driverDoorAjar = this.driverDoorAjar;
        o.passengerDoorAjar = this.passengerDoorAjar;
        o.rightrearDoorAjar = this.rightrearDoorAjar;
        o.leftrearDoorAjar = this.leftrearDoorAjar;
        o.trunkLiftgateAjar = this.trunkLiftgateAjar;
        o.flipperGlassAjar = this.flipperGlassAjar;
        o.odo = this.odo;
        o.absBrakeEvent = this.absBrakeEvent;
        o.anyImpactEvent = this.anyImpactEvent;
        o.ignitionSw = this.ignitionSw;
        o.accSw = this.accSw;
        o.lowWasherFluidWarning = this.lowWasherFluidWarning;
        o.lowBrakeFluidWarning = this.lowBrakeFluidWarning;
        o.tirePressureIndicationStatus = this.tirePressureIndicationStatus;
        o.smartEntryErrorCode = this.smartEntryErrorCode;
        o.parkBrakeEngaged = this.parkBrakeEngaged;
        o.overheating = this.overheating;
        o.chargingSystemFailure = this.chargingSystemFailure;
        o.ascIndicatorLamp = this.ascIndicatorLamp;
        o.absFaultyIndicatorLamp = this.absFaultyIndicatorLamp;
        o.milOn = this.milOn;
        return o;
    }

    public static void update(long updateFlag1, long updateFlag2, long updateFlag3, long updateFlag4, VCanInfo canInfo, VCanInfo info) {
        if ((1 & updateFlag1) != 0) {
            canInfo.vehicleSpeed = info.vehicleSpeed;
        }
        if ((2 & updateFlag1) != 0) {
            canInfo.yawRate = info.yawRate;
        }
        if ((4 & updateFlag1) != 0) {
            canInfo.steeringAngle = info.steeringAngle;
        }
        if ((8 & updateFlag1) != 0) {
            canInfo.acceleratorPedalPosition1 = info.acceleratorPedalPosition1;
        }
        if ((16 & updateFlag1) != 0) {
            canInfo.acceleratorPedalPosition2 = info.acceleratorPedalPosition2;
        }
        if ((32 & updateFlag1) != 0) {
            canInfo.brakeSW = info.brakeSW;
        }
        if ((64 & updateFlag1) != 0) {
            canInfo.turningIndicatorLamp = info.turningIndicatorLamp;
        }
        if ((128 & updateFlag1) != 0) {
            canInfo.ecoStatus = info.ecoStatus;
        }
        if ((256 & updateFlag1) != 0) {
            canInfo.longitudinalG = info.longitudinalG;
        }
        if ((512 & updateFlag1) != 0) {
            canInfo.lateralG = info.lateralG;
        }
        if ((1024 & updateFlag1) != 0) {
            canInfo.engineSpeed = info.engineSpeed;
        }
        if ((2048 & updateFlag1) != 0) {
            canInfo.engineCoolantTemperature = info.engineCoolantTemperature;
        }
        if ((4096 & updateFlag1) != 0) {
            canInfo.headway = info.headway;
        }
        if ((8192 & updateFlag1) != 0) {
            canInfo.averageInjectionFuel = info.averageInjectionFuel;
        }
        if ((16384 & updateFlag1) != 0) {
            canInfo.fuelConsumption = info.fuelConsumption;
        }
        if ((32768 & updateFlag1) != 0) {
            canInfo.moduleTemperature1 = info.moduleTemperature1;
        }
        if ((65536 & updateFlag1) != 0) {
            canInfo.moduleTemperature2 = info.moduleTemperature2;
        }
        if ((131072 & updateFlag1) != 0) {
            canInfo.moduleTemperature3 = info.moduleTemperature3;
        }
        if ((FLAG_CAN1_MODULE_TEMPERATURE4 & updateFlag1) != 0) {
            canInfo.moduleTemperature4 = info.moduleTemperature4;
        }
        if ((FLAG_CAN1_MODULE_TEMPERATURE5 & updateFlag1) != 0) {
            canInfo.moduleTemperature5 = info.moduleTemperature5;
        }
        if ((FLAG_CAN1_MODULE_TEMPERATURE6 & updateFlag1) != 0) {
            canInfo.moduleTemperature6 = info.moduleTemperature6;
        }
        if ((FLAG_CAN1_BATTERY_CELL_MAXIMUM_TEMPERATURE & updateFlag1) != 0) {
            canInfo.batteryCellMaximumTemperature = info.batteryCellMaximumTemperature;
        }
        if ((FLAG_CAN1_BATTERY_CELL_MINIMUM_TEMPERATURE & updateFlag1) != 0) {
            canInfo.batteryCellMinimumTemperature = info.batteryCellMinimumTemperature;
        }
        if ((1 & updateFlag2) != 0) {
            canInfo.stateOfCharge = info.stateOfCharge;
        }
        if ((2 & updateFlag2) != 0) {
            canInfo.batteryCurrent = info.batteryCurrent;
        }
        if ((4 & updateFlag2) != 0) {
            canInfo.batteryVoltage = info.batteryVoltage;
        }
        if ((8 & updateFlag2) != 0) {
            canInfo.airConditionerPower = info.airConditionerPower;
        }
        if ((16 & updateFlag2) != 0) {
            canInfo.heaterPower = info.heaterPower;
        }
        if ((32 & updateFlag2) != 0) {
            canInfo.shiftPosition = info.shiftPosition;
        }
        if ((64 & updateFlag2) != 0) {
            canInfo.currentGearPosition = info.currentGearPosition;
        }
        if ((128 & updateFlag2) != 0) {
            canInfo.acSwStatus = info.acSwStatus;
        }
        if ((256 & updateFlag2) != 0) {
            canInfo.freshRecirculationModeStatus = info.freshRecirculationModeStatus;
        }
        if ((512 & updateFlag2) != 0) {
            canInfo.prndlDisplayRequest = info.prndlDisplayRequest;
        }
        if ((1024 & updateFlag2) != 0) {
            canInfo.acTempDialSw = info.acTempDialSw;
        }
        if ((2048 & updateFlag2) != 0) {
            canInfo.blowerFanOutput = info.blowerFanOutput;
        }
        if ((4096 & updateFlag2) != 0) {
            canInfo.acModeStatus = info.acModeStatus;
        }
        if ((8192 & updateFlag2) != 0) {
            canInfo.cabinTemp = info.cabinTemp;
        }
        if ((16384 & updateFlag2) != 0) {
            canInfo.ambienceTemp = info.ambienceTemp;
        }
        if ((32768 & updateFlag2) != 0) {
            canInfo.vehicleIdentificationNumber = info.vehicleIdentificationNumber;
        }
        if ((65536 & updateFlag2) != 0) {
            canInfo.modelYear = info.modelYear;
        }
        if ((131072 & updateFlag2) != 0) {
            canInfo.vehicleLine = info.vehicleLine;
        }
        if ((1 & updateFlag3) != 0) {
            canInfo.fuelLevel = info.fuelLevel;
        }
        if ((2 & updateFlag3) != 0) {
            canInfo.headLampStatus = info.headLampStatus;
        }
        if ((4 & updateFlag3) != 0) {
            canInfo.roomLampStatus = info.roomLampStatus;
        }
        if ((8 & updateFlag3) != 0) {
            canInfo.wiperSwInfomation = info.wiperSwInfomation;
        }
        if ((16 & updateFlag3) != 0) {
            canInfo.parkingSw = info.parkingSw;
        }
        if ((32 & updateFlag3) != 0) {
            canInfo.averageFuelConsumption = info.averageFuelConsumption;
        }
        if ((64 & updateFlag3) != 0) {
            canInfo.distanceToEmptyFuel = info.distanceToEmptyFuel;
        }
        if ((128 & updateFlag3) != 0) {
            canInfo.doorStatus = info.doorStatus;
        }
        if ((256 & updateFlag3) != 0) {
            canInfo.driverDoorAjar = info.driverDoorAjar;
        }
        if ((512 & updateFlag3) != 0) {
            canInfo.passengerDoorAjar = info.passengerDoorAjar;
        }
        if ((1024 & updateFlag3) != 0) {
            canInfo.rightrearDoorAjar = info.rightrearDoorAjar;
        }
        if ((2048 & updateFlag3) != 0) {
            canInfo.leftrearDoorAjar = info.leftrearDoorAjar;
        }
        if ((4096 & updateFlag3) != 0) {
            canInfo.trunkLiftgateAjar = info.trunkLiftgateAjar;
        }
        if ((8192 & updateFlag3) != 0) {
            canInfo.flipperGlassAjar = info.flipperGlassAjar;
        }
        if ((1 & updateFlag4) != 0) {
            canInfo.odo = info.odo;
        }
        if ((2 & updateFlag4) != 0) {
            canInfo.absBrakeEvent = info.absBrakeEvent;
        }
        if ((4 & updateFlag4) != 0) {
            canInfo.anyImpactEvent = info.anyImpactEvent;
        }
        if ((8 & updateFlag4) != 0) {
            canInfo.ignitionSw = info.ignitionSw;
        }
        if ((16 & updateFlag4) != 0) {
            canInfo.accSw = info.accSw;
        }
        if ((32 & updateFlag4) != 0) {
            canInfo.lowWasherFluidWarning = info.lowWasherFluidWarning;
        }
        if ((64 & updateFlag4) != 0) {
            canInfo.lowBrakeFluidWarning = info.lowBrakeFluidWarning;
        }
        if ((128 & updateFlag4) != 0) {
            canInfo.tirePressureIndicationStatus = info.tirePressureIndicationStatus;
        }
        if ((512 & updateFlag4) != 0) {
            canInfo.parkBrakeEngaged = info.parkBrakeEngaged;
        }
        if ((2048 & updateFlag4) != 0) {
            canInfo.chargingSystemFailure = info.chargingSystemFailure;
        }
        if ((1024 & updateFlag4) != 0) {
            canInfo.overheating = info.overheating;
        }
        if ((256 & updateFlag4) != 0) {
            canInfo.smartEntryErrorCode = info.smartEntryErrorCode;
        }
        if ((4096 & updateFlag4) != 0) {
            canInfo.ascIndicatorLamp = info.ascIndicatorLamp;
        }
        if ((8192 & updateFlag4) != 0) {
            canInfo.absFaultyIndicatorLamp = info.absFaultyIndicatorLamp;
        }
        if ((16384 & updateFlag4) != 0) {
            canInfo.milOn = info.milOn;
        }
    }
}
