package com.company.dento.report;

import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Log4j2
@Component
public class JasperWriter {

    public File writeReport(final File templateFile, final Map<String, Object> parameters)
            throws IOException, JRException {

        Preconditions.checkNotNull(templateFile, "Template file cannot be null");
        Preconditions.checkNotNull(parameters, "Parameters map cannot be null");
        log.info("Writing report {}", templateFile.getName());

        final JasperPrint jasperPrint = JasperFillManager.fillReport(new FileInputStream(templateFile),
                parameters, new JREmptyDataSource());

        return exportReport(jasperPrint);
    }

    private File exportReport(final JasperPrint jasperPrint) throws JRException, IOException {
        final File outputFile = Files.createTempFile("report", ".pdf").toFile();
        final JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));

        final SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        final SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("Dento");
        exportConfig.setEncrypted(false);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport();

        return outputFile;
    }
}
