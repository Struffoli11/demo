package groupquattro.demo.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;

import java.util.Map;

@Mapper
public interface UtilityMapper {
    @MapMapping(keyTargetType = String.class, valueTargetType = Double.class)
    Map<String, String> getMap(Map<String, Double> source);

    @InheritInverseConfiguration
    Map<String, Double> getStringAndDoubleMap(Map<String, String> source);
}
