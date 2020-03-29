DROP PROCEDURE IF EXISTS pomnimva_health.p_insert_weather;
delimiter //
CREATE PROCEDURE pomnimva_health.`p_insert_weather`(IN in_dt varchar(10), 
                                     IN in_cloudy varchar(20), 
                                     IN in_tempDay int, 
                                     IN in_tempNight int, 
                                     IN in_pressure int, 
                                     IN in_humidity int, 
                                     IN in_windDir varchar(20), 
                                     IN in_windSpeed int,
                                     IN in_moonPhase varchar(20),
                                     IN in_geoField varchar(20),
                                     IN in_zodiak varchar(20),
                                     IN in_moonDay int,
                                     IN in_moonVisibility float,
                                     IN in_sunRise time,
                                     IN in_sunDown time,
                                     IN in_moonRise time,
                                     IN in_moonDown time,
                                     IN in_dayLength time
                                    )
    SQL SECURITY INVOKER
    COMMENT 'A procedure'
BEGIN 
  declare vRecordCount int;
  declare vProcName text;
  declare msg longtext;
  set vRecordCount := 0;
  set vProcName := 'p_insert_weather';
  set msg := concat('procedure: ',vProcName, '; '
                   ,'parameters: '
                   ,'in_dt = ', ifNull(in_dt,'null'), '; '
                   ,'in_cloudy = ', ifNull(in_cloudy,'null'), '; '
                   ,'in_tempDay = ', ifNull(in_tempDay,'null'), '; '
                   ,'in_tempNight = ', ifNull(in_tempNight,'null'), '; '
                   ,'in_pressure = ', ifNull(in_pressure,'null'), '; '
                   ,'in_humidity = ', ifNull(in_humidity,'null'), '; '
                   ,'in_windDir = ', ifNull(in_windDir,'null'), '; '
                   ,'in_windSpeed = ', ifNull(in_windSpeed,'null'), '; '
                   ,'in_moonPhase = ', ifNull(in_moonPhase,'null'), '; '
                   ,'in_geoField = ', ifNull(in_geoField,'null'), '; '
                   ,'in_zodiak = ', ifNull(in_zodiak,'null'), '; '
                   ,'in_moonDay = ', ifNull(in_moonDay,'null'), '; '
                   ,'in_moonVisibility = ', ifNull(in_moonVisibility,'null'), '; '
                   ,'in_sunRise = ', ifNull(in_sunRise,'null'), '; '
                   ,'in_sunDown = ', ifNull(in_sunDown,'null'), '; '
                   ,'in_moonRise = ', ifNull(in_moonRise,'null'), '; '
                   ,'in_moonDown = ', ifNull(in_moonDown,'null'), '; '
                   ,'in_dayLength = ', ifNull(in_dayLength,'null'), '; ');
  
  insert 
    into tbllog(msg)
    values(msg);
  
  select count(*)
  into vRecordCount
  from pomnimva_health.tblweather
  where dt = STR_TO_DATE(in_dt,'%d.%m.%Y');
    
  if vRecordCount = 0 then 
    insert 
      into pomnimva_health.tblweather(dt,
                                cloudyTypeId,
                                tempday,
                                tempnight,
                                pressure,
                                humidity,
                                windDirectionId,
                                windSpeed,
                                moonPhase,
                                geoField,
                                zodiakId,
                                moonDay,
                                moonVisibility,
                                sunRise,
                                sunDown,
                                moonRise,
                                moonDown,
                                dayLength)
      values(STR_TO_DATE(in_dt,'%d.%m.%Y'),
             (select id 
              from tblcloudytype 
              where upper(cloudyTypeName) = upper(in_cloudy)),
             in_tempDay,
             in_tempNight,
             in_pressure,
             in_humidity,
             (select id 
              from tblwinddirections 
              where upper(windDirectionShortName) = upper(in_windDir)),
             in_windSpeed,
             in_moonPhase,
             in_geoField,
             (select id
              from tblzodiaks
              where upper(zodiakName2) = upper(in_zodiak)),
             in_moonDay,
             in_moonVisibility,
             in_sunRise,
             in_sunDown,
             in_moonRise,
             in_moonDown,
             in_dayLength);
  else
    update pomnimva_health.tblweather set
      cloudyTypeId    = case when in_cloudy is null then cloudyTypeId else (select id from tblcloudytype where upper(cloudyTypeName) = upper(in_cloudy)) end,
      tempday         = ifNull(in_tempDay, tempDay),
      tempnight       = ifNull(in_tempNight, tempNight),
      pressure        = ifNull(in_pressure, pressure),
      humidity        = ifNull(in_humidity, humidity),
      windDirectionId = case when in_windDir is null then windDirectionId else (select id from tblwinddirections where upper(windDirectionShortName) = upper(in_windDir)) end,
      windSpeed       = ifNull(in_windSpeed, windSpeed),
      moonPhase       = ifNull(in_moonPhase, moonPhase),
      geoField        = ifNull(in_geoField, geoField),
      zodiakId        = case when in_zodiak is null then zodiakId else (select id from tblzodiaks where upper(zodiakName2) = upper(in_zodiak)) end,
      moonDay         = ifNull(in_moonDay, moonDay),
      moonVisibility  = ifNull(in_moonVisibility, moonVisibility),
      sunRise         = ifNull(in_sunRise, sunRise),
      sunDown         = ifNull(in_sunDown, sunDown),
      moonRise        = ifNull(in_moonRise, moonRise),
      moonDown        = ifNull(in_moonDown, moonDown),
      daylength       = ifNull(in_dayLength, daylength)
    where dt = STR_TO_DATE(in_dt,'%d.%m.%Y');
  end if;
END;//
