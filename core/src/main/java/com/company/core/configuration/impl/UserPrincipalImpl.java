package com.company.core.configuration.impl;

import com.company.core.configuration.UserPrincipal;
import com.company.core.configuration.UserPrincipalConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = UserPrincipal.class)
@Designate(
        ocd = UserPrincipalConfiguration.class
)
public class UserPrincipalImpl implements UserPrincipal {

    private UserPrincipalConfiguration userPrincipalConfiguration;

    @Activate
    @Modified
    protected final void activate(UserPrincipalConfiguration config) {
        this.userPrincipalConfiguration = config;
    }

    @Override
    public String getUserMappingPrincipal() {
        return this.userPrincipalConfiguration.user_mapping_principal();
    }
}
