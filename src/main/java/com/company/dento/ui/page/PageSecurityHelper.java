package com.company.dento.ui.page;

import com.company.dento.model.business.Material;
import com.company.dento.ui.page.edit.*;
import com.company.dento.ui.page.list.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

public class PageSecurityHelper {

    private static final Map<Class, List> PAGE_ROLES_MAP = new HashMap<>() {
        {
            put(StartPage.class, Arrays.asList("ADMIN", "USER", "TECHNICIAN"));
            put(RootPage.class, Arrays.asList("ADMIN", "USER", "TECHNICIAN"));
            put(OrdersPage.class, Arrays.asList("ADMIN", "USER", "TECHNICIAN"));
            put(ExecutionsPage.class, Arrays.asList("ADMIN", "USER", "TECHNICIAN"));
            put(MaterialsPage.class, Arrays.asList("ADMIN", "USER", "TECHNICIAN"));
            put(CalendarPage.class, Arrays.asList("ADMIN", "USER", "TECHNICIAN"));
            put(OrderEditPage.class, Arrays.asList("ADMIN", "USER", "TECHNICIAN"));

            put(GeneralDataEditPage.class, Collections.singletonList("ADMIN"));
            put(ClinicsPage.class, Collections.singletonList("ADMIN"));
            put(DoctorsPage.class, Collections.singletonList("ADMIN"));
            put(ExecutionTemplatesPage.class, Collections.singletonList("ADMIN"));
            put(JobTemplatesPage.class, Collections.singletonList("ADMIN"));
            put(MaterialTemplatesPage.class, Collections.singletonList("ADMIN"));
            put(SampleTemplatesPage.class, Collections.singletonList("ADMIN"));
            put(ToothColorsPage.class, Collections.singletonList("ADMIN"));
            put(ToothOptionsPage.class, Collections.singletonList("ADMIN"));
            put(UsersPage.class, Collections.singletonList("ADMIN"));

            put(ClinicEditPage.class, Collections.singletonList("ADMIN"));
            put(DoctorEditPage.class, Collections.singletonList("ADMIN"));
            put(ExecutionTemplateEditPage.class, Collections.singletonList("ADMIN"));
            put(JobTemplateEditPage.class, Collections.singletonList("ADMIN"));
            put(MaterialTemplateEditPage.class, Collections.singletonList("ADMIN"));
            put(SampleTemplateEditPage.class, Collections.singletonList("ADMIN"));
            put(ToothColorEditPage.class, Collections.singletonList("ADMIN"));
            put(ToothOptionEditPage.class, Collections.singletonList("ADMIN"));
            put(UserEditPage.class, Collections.singletonList("ADMIN"));
        }
    };

    public static boolean hasPageAccess(final Class pageClass) {
        return !Collections.disjoint(PAGE_ROLES_MAP.getOrDefault(pageClass, Collections.emptyList()),
                getCurrentUserAuthorities());
    }

    static Set<String> getCurrentUserAuthorities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<String> authorities = new HashSet<>();
        if(auth!= null) {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            if(user != null && user.getAuthorities() != null) {
                for(GrantedAuthority ga : user.getAuthorities()) {
                    authorities.add(ga.getAuthority());
                }
            }
        }
        return authorities;
    }

}
