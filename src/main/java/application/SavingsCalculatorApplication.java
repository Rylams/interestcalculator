package application;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SavingsCalculatorApplication extends Application {

    public static void main(String[] args) {
        launch(SavingsCalculatorApplication.class);
        System.out.println("Hello world!");
    }

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane layout2 = new BorderPane();
        BorderPane layout = new BorderPane();

        final int[] time = new int[1];

        Label timeInputLabel = new Label("Olá, digite o prazo do investimento (em meses)");
        TextField timeField = new TextField("12");
        Button submitTime = new Button("Enviar");
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                time[0] = Integer.parseInt(timeField.getText());
                //cria o gráfico
                VBox slidersContainer = new VBox();
                NumberAxis xAxis = new NumberAxis(0, time[0], 1);
                NumberAxis yAxis = new NumberAxis();

                LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
                layout.setCenter(lineChart);

                //Slider do capital a ser investido

                BorderPane savingsContainer = new BorderPane();
                Label savingsLabel = new Label("Capital");
                Label savingsSliderLabel = new Label("0.0");
                savingsContainer.setLeft(savingsLabel);
                Label interestValueLabel = new Label("0");

                Slider savingsSlider = new Slider();
                savingsSlider.setMin(0);
                savingsSlider.setMax(100000);
                savingsSlider.setBlockIncrement(1000);
                savingsSlider.setSnapToTicks(true);


                savingsContainer.setCenter(savingsSlider);
                savingsContainer.setRight(savingsSliderLabel);
                slidersContainer.getChildren().add(savingsContainer);


                //adiciona listener para savingsSlider
                savingsSlider.valueProperty().addListener(
                        new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                savingsSliderLabel.setText("" + newValue.doubleValue());
                            }
                        }
                );
                savingsSlider.valueProperty().addListener((obs, oldval, newVal) ->
                        savingsSlider.setValue(Math.round(newVal.doubleValue())));


                //define taxa de juros
                BorderPane interestContainer = new BorderPane();
                Label interestLabel = new Label("Taxa de juros(%)");
                Label interestSliderLabel = new Label("0.0");

                Slider interestSlider = new Slider(0, 20, 0);
                interestSlider.setMinorTickCount(1);
                interestSlider.setBlockIncrement(0.25);
                interestSlider.setShowTickMarks(true);
                interestSlider.setShowTickLabels(true);

                interestContainer.setLeft(interestLabel);
                interestContainer.setCenter(interestSlider);
                interestContainer.setRight(interestSliderLabel);
                slidersContainer.getChildren().add(interestContainer);

                XYChart.Series interestValue = new XYChart.Series();
                interestValue.setName("Capital acrescido de juros");

                lineChart.getData().add(interestValue);

                interestSlider.valueProperty().addListener(
                        new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                interestSliderLabel.setText("" + newValue.doubleValue());
                                interestValue.getData().clear();
                                double balance = Double.parseDouble(savingsSliderLabel.getText());
                                for (int i = 0; i < time[0] + 1; i++) {
                                    interestValue.getData().add(new XYChart.Data<>(i, balance));
                                    balance = (balance) * (1.0 + newValue.doubleValue() / 100.0);
                                }
                                interestValueLabel.setText("O valor final é " + Math.round(balance) + "$ sendo " + Math.round(100 * (balance - Double.parseDouble(savingsSliderLabel.getText())) / balance) + "% é composto de juros");
                            }
                        });

                //cria layout de apresentação do gráfico

                layout.setTop(slidersContainer);
                layout.setBottom(interestValueLabel);
                Scene view = new Scene(layout, 500, 550);
                stage.setScene(view);
                stage.show();
            }
        };

        submitTime.setOnAction(event);

        layout2.setTop(timeInputLabel);
        layout2.setCenter(timeField);
        layout2.setBottom(submitTime);

        Scene view = new Scene(layout2, 300, 200);
        stage.setScene(view);
        stage.show();
    }

}
