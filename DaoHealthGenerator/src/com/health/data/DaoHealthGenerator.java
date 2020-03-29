package com.health.data;

import de.greenrobot.daogenerator.*;

/**
 * Created by alex sh on 23.07.2015.
 */
public class DaoHealthGenerator {

    public static void main(String[] args) throws Exception {
        System.out.println("Working Directory = " +  System.getProperty("user.dir"));
        Schema schema = new Schema(1000, "com.health.data");
        schema.enableKeepSectionsByDefault();
        
        addHealthSchema(schema);
        new DaoGenerator().generateAll(schema, "generatedDTO");
    }    

    public static final String ID_COLUMN = "id";
    public static final String ID_SERVER_COLUMN = "serverid";
    public static final String ID_USER_COLUMN = "userid";
    public static final String ID_ROW_COLUMN = "rowid";
    public static final String NAME_COLUMN = "name";

    private static void addHealthSchema(Schema schema) {
        //Schema schema = new Schema(1, "by.health.dto");
        //schema.enableKeepSectionsByDefault();
        Entity baseDTO = schema.addEntity("BaseDTO");
        extendFromBaseDTO(baseDTO);

        Entity baseSyncDTO = schema.addEntity("BaseSyncDTO");
        extendFromBaseSyncDTO(baseSyncDTO);

        Entity baseUserSyncDTO = schema.addEntity("BaseUserSyncDTO");
        extendFromBaseUserSyncDTO(baseUserSyncDTO);

        Entity bodyFeelingType = schema.addEntity("BodyFeelingType");
        extendFromBaseDTO(bodyFeelingType);
        bodyFeelingType.setTableName("tblbodyfeelingtype");
        bodyFeelingType.addStringProperty(NAME_COLUMN).columnName("name");
        bodyFeelingType.implementsInterface("IDictionaryDTO");

        Entity bodyComplaintType = schema.addEntity("BodyComplaintType");
        bodyComplaintType.setTableName("tblbodycomplainttype");
        extendFromBaseDTO(bodyComplaintType);
        bodyComplaintType.addStringProperty("Name").columnName("name");
        bodyComplaintType.implementsInterface("IGridGroup");

        Entity bodyRegion = schema.addEntity("BodyRegion");
        extendFromBaseDTO(bodyRegion);
        bodyRegion.setTableName("tblbodyregion");
        bodyRegion.addStringProperty(NAME_COLUMN).columnName("name");
        bodyRegion.addStringProperty("FullName").columnName("fullname");
        bodyRegion.implementsInterface("IDictionaryDTO");
        Property FK_complaintid = bodyRegion.addLongProperty("BodyComplaintTypeId").columnName("complaintid").index().getProperty();
        bodyRegion.addToOne(bodyComplaintType, FK_complaintid);

        Entity customBodyFeelingType = schema.addEntity("CustomBodyFeelingType");
        extendFromBaseUserSyncDTO(customBodyFeelingType);
        customBodyFeelingType.setTableName("tblcustombodyfeelingtype");
        customBodyFeelingType.addStringProperty(NAME_COLUMN).columnName("name");
        customBodyFeelingType.implementsInterface("IDictionaryDTO");
        customBodyFeelingType.implementsInterface("IBaseUserSyncDTO");
        



        Entity bodyFeeling = schema.addEntity("BodyFeeling");
        extendFromBaseUserSyncDTO(bodyFeeling);
        bodyFeeling.setTableName("tblbodyfeeling");
        bodyFeeling.addIntProperty("X").columnName("x");
        bodyFeeling.addIntProperty("Y").columnName("y");
        bodyFeeling.addDateProperty("StartDate").columnName("dt");
        Property feelingTypeId = bodyFeeling.addLongProperty("FeelingTypeId").columnName("feelingtypeid").index().getProperty();
        Property customFeelingTypeId = bodyFeeling.addLongProperty("CustomFeelingTypeId").columnName("customfeelingtypeid").index().getProperty();
        Property bodyRegionId = bodyFeeling.addLongProperty("BodyRegionId").columnName("bodyregionid").index().getProperty();
        bodyFeeling.implementsInterface("IFeeling");
        bodyFeeling.addToOne(bodyFeelingType, feelingTypeId);
        bodyFeeling.addToOne(customBodyFeelingType, customFeelingTypeId);
        bodyFeeling.addToOne(bodyRegion, bodyRegionId);

        Entity country = schema.addEntity("Country");
        country.setTableName("tblcountry");
        extendFromBaseDTO(country);
        country.addStringProperty(NAME_COLUMN).columnName("name");
        country.addStringProperty("Code").columnName("code");
        country.addStringProperty("Culture").columnName("culture");
        country.addStringProperty("Name_ru").columnName("name_ru");

        Entity city = schema.addEntity("City");
        city.setTableName("tblcity");
        extendFromBaseDTO(city);
        city.addStringProperty(NAME_COLUMN).columnName("name");
        city.addStringProperty("CountryCode").columnName("country_code");
        city.addStringProperty("Name_ru").columnName("name_ru");
        city.addDoubleProperty("Lat").columnName("lat");
        city.addDoubleProperty("Lng").columnName("lng");
        Property countryId = city.addLongProperty("CountryId").columnName("country_id").index().getProperty();
        city.addToOne(country, countryId);

        Entity question = schema.addEntity("RecoveryAccountQuestion");
        question.setTableName("tblrecoveryaccount_question");
        extendFromBaseDTO(question);
        question.addStringProperty("Question").columnName("question");

        Entity user = schema.addEntity("User");
        user.setTableName("tbluser");
        extendFromBaseDTO(user);
        user.addIntProperty("IsActive").columnName("IsActive");
        user.addIntProperty("IsStorePassword").columnName("IsStorePassword");
        user.addStringProperty("Login").columnName("Login");
        user.addStringProperty("Password").columnName("Password");
        user.addStringProperty("FName").columnName("FName");
        user.addStringProperty("LName").columnName("LName");
        user.addStringProperty("MName").columnName("MName");
        user.addDateProperty("BirthDate").columnName("BirthDate");
        user.addDateProperty("CreateDate").columnName("CreateDate");
        user.addIntProperty("Sex").columnName("Sex");
        user.addIntProperty("IsAutoSync").columnName("IsAutoSync");
        user.addIntProperty("PeriodSyncData").columnName("PeriodSyncData");
        user.addDateProperty("SyncDate").columnName("SyncDate");
        Property cityId = user.addLongProperty("CityId").columnName("cityid").index().getProperty();
        Property questionId1 = user.addLongProperty("Question1").columnName("question1").index().getProperty();
        Property questionId2 = user.addLongProperty("Question2").columnName("question2").index().getProperty();
        user.addIntProperty("MaritalStatusId").columnName("MaritalStatusId");
        user.addIntProperty("SocialStatusId").columnName("SocialStatusId");
        user.addIntProperty("Height").columnName("Height");
        user.addIntProperty("Weight").columnName("Weight");
        user.addIntProperty("PressureId").columnName("PressureId");
        user.addIntProperty("FootDistance").columnName("FootDistance");
        user.addIntProperty("SleepTime").columnName("SleepTime");
        user.addToOne(city, cityId);
        user.addToOne(question, questionId1, "ReqoveryQuestion1");
        user.addToOne(question, questionId2, "ReqoveryQuestion2");



        Entity commonFeelingGroup = schema.addEntity("CommonFeelingGroup");
        commonFeelingGroup.setTableName("tblcommonfeelinggroup");
        extendFromBaseDTO(commonFeelingGroup);
        commonFeelingGroup.addStringProperty("Name").columnName("name");
        commonFeelingGroup.implementsInterface("IGridGroup");


        Entity unitDimension = schema.addEntity("UnitDimension");
        unitDimension.setTableName("tblunitdimension");
        extendFromBaseDTO(unitDimension);
        unitDimension.addStringProperty("Name").columnName("name");

        Entity commonFeelingType = schema.addEntity("CommonFeelingType");
        commonFeelingType.setTableName("tblcommonfeelingtype");
        extendFromBaseDTO(commonFeelingType);
        commonFeelingType.addStringProperty("Name").columnName("name");
        commonFeelingType.addStringProperty("FullName").columnName("fullname");
        commonFeelingType.addIntProperty("OrdinalNumber").columnName("ordinalnumber");
        commonFeelingType.addIntProperty("Status").columnName("status");
        Property unitDimensionId = commonFeelingType.addLongProperty("UnitDimensionId").columnName("unitid").index().getProperty();
        commonFeelingType.addToOne(unitDimension, unitDimensionId);
        Property commonFeelingGroupId = commonFeelingType.addLongProperty("CommonFeelingGroupId").columnName("feelinggroupid").index().getProperty();
        commonFeelingType.addToOne(commonFeelingGroup, commonFeelingGroupId);
        commonFeelingType.implementsInterface("IGridItem", "IGridGroup");

        Entity customCommonFeelingType = schema.addEntity("CustomCommonFeelingType");
        customCommonFeelingType.setTableName("tblcustomcommonfeelingtype");
        extendFromBaseUserSyncDTO(customCommonFeelingType);
        customCommonFeelingType.addStringProperty("Name").columnName("name");
        customCommonFeelingType.addStringProperty("FullName").columnName("fullname");
        customCommonFeelingType.addIntProperty("OrdinalNumber").columnName("ordinalnumber");
        customCommonFeelingType.addIntProperty("Status").columnName("status");
        Property unitDimensionIdNew = customCommonFeelingType.addLongProperty("UnitDimensionId").columnName("unitid").index().getProperty();
        customCommonFeelingType.addToOne(unitDimension, unitDimensionIdNew);
        Property commonFeelingGroupIdNew = customCommonFeelingType.addLongProperty("CommonFeelingGroupId").columnName("feelinggroupid").index().getProperty();
        customCommonFeelingType.addToOne(commonFeelingGroup, commonFeelingGroupIdNew);
        customCommonFeelingType.implementsInterface("IGridItem");

        Entity commonFeeling = schema.addEntity("CommonFeeling");
        commonFeeling.setTableName("tblcommonfeeling");
        extendFromBaseUserSyncDTO(commonFeeling);
        commonFeeling.addDateProperty("StartDate").columnName("dt");
        commonFeeling.addDoubleProperty("Value1").columnName("value1");
        commonFeeling.addDoubleProperty("Value2").columnName("value2");
        commonFeeling.addDoubleProperty("Value3").columnName("value3");
        Property commonFeelingTypeId = commonFeeling.addLongProperty("CommonFeelingTypeId").columnName("feelingtypeid").index().getProperty();
        commonFeeling.addToOne(commonFeelingType, commonFeelingTypeId);
        Property customCommonFeelingTypeId = commonFeeling.addLongProperty("CustomCommonFeelingTypeId").columnName("customfeelingtypeid").index().getProperty();
        commonFeeling.addToOne(customCommonFeelingType, customCommonFeelingTypeId);
        commonFeeling.implementsInterface("IFeeling","IGridItemValue");


        Entity factorGroup = schema.addEntity("FactorGroup");
        factorGroup.setTableName("tblfactorgroup");
        extendFromBaseDTO(factorGroup);
        factorGroup.addStringProperty("Name").columnName("name");
        factorGroup.implementsInterface("IGridGroup");

        Entity factorType = schema.addEntity("FactorType");
        factorType.setTableName("tblfactortype");
        extendFromBaseDTO(factorType);
        factorType.addStringProperty("Name").columnName("name");
        factorType.addIntProperty("OrdinalNumber").columnName("ordinalnumber");
        factorType.addIntProperty("Status").columnName("status");
        Property unitDimensionId_FT = factorType.addLongProperty("UnitDimensionId").columnName("unitid").index().getProperty();
        factorType.addToOne(unitDimension, unitDimensionId_FT);
        Property factorGroupId = factorType.addLongProperty("FactorGroupId").columnName("factorgroupid").index().getProperty();
        factorType.addToOne(factorGroup, factorGroupId);
        factorType.implementsInterface("IGridItem");



        Entity customFactorType = schema.addEntity("CustomFactorType");
        customFactorType.setTableName("tblcustomfactortype");
        extendFromBaseUserSyncDTO(customFactorType);
        customFactorType.addStringProperty("Name").columnName("name");
        customFactorType.addStringProperty("FullName").columnName("fullname");
        customFactorType.addIntProperty("OrdinalNumber").columnName("ordinalnumber");
        customFactorType.addIntProperty("Status").columnName("status");
        Property unitDimensionId_CFT = customFactorType.addLongProperty("UnitDimensionId").columnName("unitid").index().getProperty();
        customFactorType.addToOne(unitDimension, unitDimensionId_CFT);
        Property factorGroupId_C = customFactorType.addLongProperty("FactorGroupId").columnName("factorgroupid").index().getProperty();
        customFactorType.addToOne(factorGroup, factorGroupId_C);
        customFactorType.implementsInterface("IGridItem");



        Entity factor = schema.addEntity("Factor");
        factor.setTableName("tblfactor");
        extendFromBaseUserSyncDTO(factor);
        factor.addDateProperty("StartDate").columnName("dt");
        factor.addDoubleProperty("Value1").columnName("value1");
        factor.addDoubleProperty("Value2").columnName("value2");
        factor.addDoubleProperty("Value3").columnName("value3");
        Property factorTypeId = factor.addLongProperty("FactorTypeId").columnName("factortypeid").index().getProperty();
        factor.addToOne(factorType, factorTypeId);
        Property customFactorTypeId = factor.addLongProperty("CustomFactorTypeId").columnName("customfactortypeid").index().getProperty();
        factor.addToOne(customFactorType, customFactorTypeId);
        factor.implementsInterface("IGridItemValue");




        Entity bodyRegion_FeelingType = schema.addEntity("BodyRegion_BodyFeelingType");
        bodyRegion_FeelingType.setTableName("tblbodyregion_feelingtype");
        extendFromBaseDTO(bodyRegion_FeelingType);
        Property br_FK = bodyRegion_FeelingType.addLongProperty("BodyRegionId").columnName("bodyregionid").index().getProperty();
        Property bft_FK = bodyRegion_FeelingType.addLongProperty("BodyFeelingTypeId").columnName("feelingtypeid").index().getProperty();
        bodyRegion_FeelingType.addToOne(bodyRegion, br_FK);
        bodyRegion_FeelingType.addToOne(bodyFeelingType, bft_FK);


        Entity cloudyType = schema.addEntity("CloudyType");
        cloudyType.setTableName("tblcloudytype");
        extendFromBaseDTO(cloudyType);
        cloudyType.addStringProperty("Name").columnName("cloudyTypeName");


        Entity windDirections = schema.addEntity("WindDirectionType");
        windDirections.setTableName("tblwinddirections");
        extendFromBaseDTO(windDirections);
        windDirections.addStringProperty("Name").columnName("winddirectionname");
        windDirections.addStringProperty("ShortName").columnName("winddirectionshortname");

        Entity zodiakType = schema.addEntity("ZodiakType");
        zodiakType.setTableName("tblzodiaks");
        extendFromBaseDTO(zodiakType);
        zodiakType.addStringProperty("Name").columnName("zodiakname");
        zodiakType.addStringProperty("Name2").columnName("zodiakname2");



        Entity complaint = schema.addEntity("Complaint");
        complaint.setTableName("tblcomplaint");
        extendFromBaseUserSyncDTO(complaint);
        complaint.addDateProperty("StartDate").columnName("dt");
        complaint.addIntProperty("Count").columnName("count");
        Property bct_FK = complaint.addLongProperty("BodyComplaintTypeId").columnName("complainttypeid").index().getProperty();
        Property cft_FK = complaint.addLongProperty("CommonFeelingTypeId").columnName("commonfeelingtypeid").index().getProperty();
        complaint.addToOne(bodyComplaintType, bct_FK);
        complaint.addToOne(commonFeelingType, cft_FK);

        Entity downloadPeriod = schema.addEntity("DownloadPeriod");
        downloadPeriod.setTableName("tbldownloadperiod");
        extendFromBaseDTO(downloadPeriod);
        downloadPeriod.addDateProperty("DateFrom").columnName("dtfrom");
        downloadPeriod.addDateProperty("DateTo").columnName("dtto");
        downloadPeriod.addDateProperty("SyncDate").columnName("syncdate");
        Property user_FK = downloadPeriod.addLongProperty("UserId").columnName("user_id").index().getProperty();
        Property city_FK = downloadPeriod.addLongProperty("CityId").columnName("city_id").index().getProperty();
        downloadPeriod.addToOne(user, user_FK);
        downloadPeriod.addToOne(city, city_FK);
        downloadPeriod.addIntProperty("TypeId").columnName("typeid");
        downloadPeriod.addIntProperty("Result").columnName("result");

        Entity geoPhysics = schema.addEntity("GeoPhysics");
        geoPhysics.setTableName("tblgeophysicshourly");
        extendFromBaseSyncDTO(geoPhysics);
        geoPhysics.addDateProperty("InfoDate").columnName("dt");
        geoPhysics.addIntProperty("KpId").columnName("kpid");
        geoPhysics.addIntProperty("Ap").columnName("ap");


        Entity helioPhysics = schema.addEntity("HelioPhysics");
        helioPhysics.setTableName("tblheliophysicsdaily");
        extendFromBaseSyncDTO(helioPhysics);
        helioPhysics.addDateProperty("InfoDate").columnName("dt");
        helioPhysics.addIntProperty("F10_7");
        helioPhysics.addIntProperty("SunspotNumber");
        helioPhysics.addIntProperty("SunspotArea");
        helioPhysics.addIntProperty("NewRegions");
        helioPhysics.addStringProperty("Xbkgd");
        helioPhysics.addIntProperty("FlaresC");
        helioPhysics.addIntProperty("FlaresM");
        helioPhysics.addIntProperty("FlaresX");
        helioPhysics.addIntProperty("FlaresS");
        helioPhysics.addIntProperty("Flares1");
        helioPhysics.addIntProperty("Flares2");
        helioPhysics.addIntProperty("Flares3");

        Entity kpIndex = schema.addEntity("KpIndex");
        kpIndex.setTableName("tblkpindex");
        extendFromBaseDTO(kpIndex);
        kpIndex.addIntProperty("IntValue").columnName("intvalue");
        kpIndex.addStringProperty("StrValue").columnName("strvalue");
        kpIndex.implementsInterface("IDictionaryDTO");


        Entity moonPhase = schema.addEntity("MoonPhase");
        moonPhase.setTableName("tblmoonphase");
        extendFromBaseSyncDTO(moonPhase);
        moonPhase.addDateProperty("InfoDate").columnName("dt");
        moonPhase.addStringProperty("MoonPhase");
        moonPhase.addLongProperty("MoonPhaseId");

        Entity moon = schema.addEntity("Moon");
        moon.setTableName("tblmoon");
        extendFromBaseSyncDTO(moon);
        moon.addDateProperty("InfoDate").columnName("dt");
        moon.addDoubleProperty("MoonVisibility");
        moon.addDateProperty("MoonOld");
        moon.addDateProperty("MoonRise");
        moon.addDateProperty("MoonSet");
        moon.addIntProperty("MoonPhaseId");

        Entity particle = schema.addEntity("Particle");
        particle.setTableName("tblparticle");
        extendFromBaseSyncDTO(particle);
        particle.addDateProperty("InfoDate").columnName("dt");
        particle.addLongProperty("Proton1MeV");
        particle.addLongProperty("Proton10MeV");
        particle.addLongProperty("Proton100MeV");
        particle.addLongProperty("Electron08MeV");
        particle.addLongProperty("Electron2MeV");

        Entity planet = schema.addEntity("Planet");
        planet.setTableName("tblplanet");
        extendFromBaseSyncDTO(planet);
        planet.addDateProperty("InfoDate").columnName("dt");
        planet.addDateProperty("MercuryRise");
        planet.addDateProperty("MercurySet");
        planet.addDateProperty("VenusRise");
        planet.addDateProperty("VenusSet");
        planet.addDateProperty("MarsRise");
        planet.addDateProperty("MarsSet");
        planet.addDateProperty("JupiterRise");
        planet.addDateProperty("JupiterSet");
        planet.addDateProperty("SaturnRise");
        planet.addDateProperty("SaturnSet");
        planet.addDateProperty("UranusRise");
        planet.addDateProperty("UranusSet");
        planet.addDateProperty("NeptuneRise");
        planet.addDateProperty("NeptuneSet");
        planet.addDateProperty("PlutonRise");
        planet.addDateProperty("PlutonSet");

        Entity solarEclipse = schema.addEntity("SolarEclipse");
        solarEclipse.setTableName("tblsolareclipse");
        extendFromBaseSyncDTO(solarEclipse);
        solarEclipse.addDateProperty("InfoDate").columnName("maximum");
        solarEclipse.addIntProperty("Phase");
        solarEclipse.addDoubleProperty("Magnitude");
        solarEclipse.addDateProperty("Contact1");
        solarEclipse.addDateProperty("Contact2");
        solarEclipse.addDateProperty("Contact3");
        solarEclipse.addDateProperty("Contact4");


        Entity sun = schema.addEntity("Sun");
        sun.setTableName("tblsun");
        extendFromBaseSyncDTO(sun);
        sun.addDateProperty("InfoDate").columnName("dt");
        sun.addDateProperty("SunRise");
        sun.addDateProperty("SunSet");
        sun.addStringProperty("DayLength");


        Entity weather = schema.addEntity("Weather");
        weather.setTableName("tblweatherhourly");
        extendFromBaseSyncDTO(weather);
        weather.addDateProperty("InfoDate").columnName("dt");
        weather.addDoubleProperty("Temperature");
        weather.addDoubleProperty("MinTemperature");
        weather.addDoubleProperty("MaxTemperature");
        weather.addDoubleProperty("Pressure");
        weather.addDoubleProperty("Humidity");
        weather.addDoubleProperty("WindSpeed");
        weather.addDoubleProperty("WindDeg");
        weather.addDoubleProperty("Clouds");
        weather.addDoubleProperty("Rain");
        weather.addDoubleProperty("Snow");

        Entity userBodyFeelingType = schema.addEntity("UserBodyFeelingType");
        userBodyFeelingType.setTableName("tbluserbodyfeelingtype");
        extendFromBaseUserSyncDTO(userBodyFeelingType);
        userBodyFeelingType.addIntProperty("Color");
        Property bodyFeelingType_FK = userBodyFeelingType.addLongProperty("BodyFeelingTypeId").columnName("BodyFeelingType_id").index().getProperty();
        userBodyFeelingType.addToOne(bodyFeelingType, bodyFeelingType_FK);


        Entity operationUserData = schema.addEntity("OperationUserData");
        operationUserData.setTableName("tbloperationuserdata");
        extendFromBaseDTO(operationUserData);
        operationUserData.addLongProperty("ClientId");
        operationUserData.addLongProperty("ServerId");
        operationUserData.addIntProperty("OperationType");
        operationUserData.addIntProperty("TableId");
        operationUserData.addDateProperty("OperationDate");
        operationUserData.addStringProperty("RowId");
        Property us_FK = operationUserData.addLongProperty("UserId").columnName("user_id").index().getProperty();
        operationUserData.addToOne(user, us_FK);
    }

    private static void extendFromBaseDTO(Entity entity){
        entity.addIdProperty().columnName(ID_COLUMN).autoincrement();
    }

    private static void extendFromBaseSyncDTO(Entity entity){
        extendFromBaseDTO(entity);
        entity.addLongProperty("ServerId").columnName(ID_SERVER_COLUMN).index().unique();
        entity.addStringProperty("RowId").columnName(ID_ROW_COLUMN).unique();
    }

    private static void extendFromBaseUserSyncDTO(Entity entity){
        extendFromBaseSyncDTO(entity);
        entity.addLongProperty("UserId").columnName(ID_USER_COLUMN).index();
    }
}
