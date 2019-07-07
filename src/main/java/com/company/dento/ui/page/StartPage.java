package com.company.dento.ui.page;

import com.company.dento.service.DataService;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

/**
 * Layout of the start page containing individual boxes of information.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@UIScope
@Route(value = "start")
public class StartPage extends Page{

	private static final long serialVersionUID = 1L;
	private final Chart pieChart = new Chart(ChartType.PIE);
	private final Chart areaChart = new Chart(ChartType.AREA);
	private final Chart barChart = new Chart(ChartType.BAR);
	private final VerticalLayout content = new VerticalLayout();
	private final FormLayout formLayout = new FormLayout();

	public StartPage(final DataService dataService) {
		super("Acasă", dataService);
		content.add(formLayout);
		initPieChart();
		initBarChart();
		initAreaChart();
		this.setContent(content);
		content.setSizeFull();
		formLayout.setWidthFull();
		content.getStyle().set("overflow-y", "auto");
		final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
		final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("700px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP);
		formLayout.setResponsiveSteps(rs1, rs2);
	}

	private void initPieChart() {
		final Configuration conf = pieChart.getConfiguration();

		conf.setTitle("Lucrari 2018");

		final Tooltip tooltip = new Tooltip();
		tooltip.setValueDecimals(1);
		tooltip.setPointFormat("{series.name}: <b>{point.percentage}%</b>");
		conf.setTooltip(tooltip);

		final PlotOptionsPie plotOptions = new PlotOptionsPie();
		plotOptions.setAllowPointSelect(true);
		plotOptions.setCursor(Cursor.POINTER);
		plotOptions.setShowInLegend(true);
		conf.setPlotOptions(plotOptions);

		final DataSeries series = new DataSeries();
		final DataSeriesItem chrome = new DataSeriesItem("Ceramica", 61.41);
		chrome.setSliced(true);
		chrome.setSelected(true);
		series.add(chrome);
		series.add(new DataSeriesItem("Acrilat", 11.84));
		series.add(new DataSeriesItem("Altele", 10.85));
		series.add(new DataSeriesItem("A1", 4.67));
		series.add(new DataSeriesItem("A2", 4.18));
		series.add(new DataSeriesItem("A3", 1.64));
		conf.setSeries(series);
		pieChart.setVisibilityTogglingDisabled(true);

		pieChart.addPointLegendItemClickListener(event -> {
			Notification.show("Legend item click" + " : " + event.getItemIndex()
					+ " : " + event.getItem().getName());
		});

		pieChart.setWidth("90%");
		pieChart.setMaxWidth("700px");
		pieChart.getStyle().set("border-style", "ridge");
		pieChart.getStyle().set("margin", "15px");
		formLayout.addFormItem(pieChart, "");
	}

	private void initAreaChart() {
		final Configuration configuration = areaChart.getConfiguration();

		configuration.setTitle("Vanzari 2018");
		configuration.setSubTitle("Preturi");

		final XAxis xAxis = configuration.getxAxis();
		xAxis.setCategories("1750", "1800", "1850", "1900", "1950", "1999", "2050");
		xAxis.setTickmarkPlacement(TickmarkPlacement.ON);

		final YAxis yAxis = configuration.getyAxis();
		yAxis.setTitle("RON");
		yAxis.getLabels().setFormatter("function () { return this.value / 1000;}");

		configuration.getTooltip().setValueSuffix(" de mii");

		PlotOptionsArea plotOptionsArea = new PlotOptionsArea();
		plotOptionsArea.setStacking(Stacking.NORMAL);
		configuration.setPlotOptions(plotOptionsArea);

		configuration.addSeries(new ListSeries("Tehnician 1", 502, 635, 809, 947, 1402, 3634, 5268));
		configuration.addSeries(new ListSeries("Tehnician 2", 106, 107, 111, 133, 221, 767, 1766));
		configuration.addSeries(new ListSeries("Tehnician 3", 163, 203, 276, 408, 547, 729, 628));
		configuration.addSeries(new ListSeries("Tehnician 4", 18, 31, 54, 156, 339, 818, 1201));
		configuration.addSeries(new ListSeries("Tehnician 5", 2, 2, 2, 6, 13, 30, 46));

		areaChart.setWidth("90%");
		areaChart.getStyle().set("border-style", "ridge");
		areaChart.getStyle().set("margin", "15px");
		content.add(areaChart);
	}

	private void initBarChart() {
		final Configuration configuration = barChart.getConfiguration();
		configuration.setTitle("Istoric comenzi");
		configuration.setSubTitle("Demo");
		barChart.getConfiguration().getChart().setType(ChartType.BAR);

		configuration.addSeries(new ListSeries("Year 1800", 107, 31, 635, 203, 2));
		configuration.addSeries(new ListSeries("Year 1900", 133, 156, 947, 408, 6));
		configuration.addSeries(new ListSeries("Year 2000", 814, 841, 3714, 727, 31));
		configuration.addSeries(new ListSeries("Year 2016", 1216, 1001, 4436, 738, 40));

		final XAxis x = new XAxis();
		x.setCategories("Africa", "America", "Asia", "Europe", "Oceania");
		configuration.addxAxis(x);

		final YAxis y = new YAxis();
		y.setMin(0);
		final AxisTitle yTitle = new AxisTitle();
		yTitle.setText("Population (millions)");
		yTitle.setAlign(VerticalAlign.HIGH);
		y.setTitle(yTitle);
		configuration.addyAxis(y);

		final Tooltip tooltip = new Tooltip();
		tooltip.setValueSuffix(" millions");
		configuration.setTooltip(tooltip);

		final PlotOptionsBar plotOptions = new PlotOptionsBar();
		DataLabels dataLabels = new DataLabels();
		dataLabels.setEnabled(true);
		plotOptions.setDataLabels(dataLabels);
		configuration.setPlotOptions(plotOptions);

		barChart.setMaxWidth("700px");
		barChart.setWidth("90%");
		barChart.getStyle().set("border-style", "ridge");
		barChart.getStyle().set("margin", "15px");
		formLayout.addFormItem(barChart, "");
	}

}