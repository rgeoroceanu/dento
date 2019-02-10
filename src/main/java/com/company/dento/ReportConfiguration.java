package com.company.dento;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

import static net.sf.jasperreports.engine.util.JRSaver.saveObject;

@Configuration
public class ReportConfiguration {

    @Value("classpath:templates/")
    private Resource templatesFolderResource;
    @Value("classpath:templates/order_template.jrxml")
    private Resource orderTemplateResource;

    @Bean
    public Resource orderReportTemplate() throws IOException, JRException {
        return compileReport(orderTemplateResource);
    }

    private Resource compileReport(final Resource templateResource) throws IOException, JRException {
        final JasperReport jasperReport = JasperCompileManager.compileReport(templateResource.getInputStream());
        final String outputPath = String.format("%s/%s.jasper", templatesFolderResource.getFile().getAbsolutePath(),
                FilenameUtils.getBaseName(templateResource.getFile().getName()));

        final File templateFile = new File(outputPath);

        saveObject(jasperReport, templateFile.getAbsolutePath());

        return new FileSystemResource(templateFile);
    }
}
