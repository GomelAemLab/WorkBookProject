package by.gomel.epam.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Service - WorkBook")
public @interface Configuration {

    @AttributeDefinition(
            name = "Principal",
            type = AttributeType.STRING
    )
    String user_mapping_principal() default "serviceCRUD";
}
