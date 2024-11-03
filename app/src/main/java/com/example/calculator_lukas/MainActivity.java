package com.example.calculator_lukas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private TextView expression;
    private double firstOperand = 0;
    private double secondOperand = 0;
    private String operator = "";
    private boolean isNewOperand = true;

    private DecimalFormat decimalFormat = new DecimalFormat("#.######");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        display = findViewById(R.id.display);
        expression = findViewById(R.id.expression); // Initialize the expression TextView

        setupNumberButtons();
        setupOperationButtons();
    }

    private void setupNumberButtons() {
        int[] numberButtonIds = {
                R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four,
                R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.decimal
        };

        View.OnClickListener listener = view -> {
            Button button = (Button) view;
            if (isNewOperand) {
                display.setText("");
                isNewOperand = false;
            }
            String currentText = display.getText().toString();
            display.setText(currentText + button.getText().toString());
        };

        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setupOperationButtons() {
        findViewById(R.id.clear).setOnClickListener(view -> clear());
        findViewById(R.id.delete).setOnClickListener(view -> deleteLastCharacter());
        findViewById(R.id.plus_minus).setOnClickListener(view -> toggleSign());
        findViewById(R.id.sq_root).setOnClickListener(view -> squareRoot());

        findViewById(R.id.add).setOnClickListener(view -> selectOperator(getString(R.string.add)));
        findViewById(R.id.subtract).setOnClickListener(view -> selectOperator(getString(R.string.subtract)));
        findViewById(R.id.multiply).setOnClickListener(view -> selectOperator(getString(R.string.multiply)));
        findViewById(R.id.divide).setOnClickListener(view -> selectOperator(getString(R.string.divide)));

        findViewById(R.id.equal).setOnClickListener(view -> calculateResult());
    }

    private void clear() {
        display.setText(getString(R.string.default_display_text));
        expression.setText("");  // Clear the expression display
        firstOperand = 0;
        secondOperand = 0;
        operator = "";
        isNewOperand = true;
    }

    private void deleteLastCharacter() {
        String text = display.getText().toString();
        if (text.length() > 1) {
            display.setText(text.substring(0, text.length() - 1));
        } else {
            display.setText(getString(R.string.default_display_text));
            isNewOperand = true;
        }
    }

    private void toggleSign() {
        String text = display.getText().toString();
        if (!text.equals(getString(R.string.default_display_text))) {
            if (text.startsWith(getString(R.string.negative_sign))) {
                display.setText(text.substring(1));
            } else {
                display.setText(getString(R.string.negative_sign) + text);
            }
        }
    }

    private void squareRoot() {
        String text = display.getText().toString();
        double value = Double.parseDouble(text);
        if (value >= 0) {
            display.setText(decimalFormat.format(Math.sqrt(value)));
            isNewOperand = true;
            expression.setText(""); // Clear the expression after displaying the square root
        } else {
            display.setText(getString(R.string.invalid_input));
        }
    }

    private void selectOperator(String selectedOperator) {
        firstOperand = Double.parseDouble(display.getText().toString());
        operator = selectedOperator;
        isNewOperand = true;

        // Update the expression TextView to show what is being calculated
        expression.setText(decimalFormat.format(firstOperand) + " " + operator);
    }

    private void calculateResult() {
        if (!operator.isEmpty()) {
            secondOperand = Double.parseDouble(display.getText().toString());
            double result = 0;

            // Perform the calculation based on the operator
            if (operator.equals(getString(R.string.add))) {
                result = firstOperand + secondOperand;
            } else if (operator.equals(getString(R.string.subtract))) {
                result = firstOperand - secondOperand;
            } else if (operator.equals(getString(R.string.multiply))) {
                result = firstOperand * secondOperand;
            } else if (operator.equals(getString(R.string.divide))) {
                if (secondOperand != 0) {
                    result = firstOperand / secondOperand;
                } else {
                    display.setText(getString(R.string.error_divide_by_zero));
                    operator = "";
                    expression.setText("");  // Clear the expression on error
                    isNewOperand = true;
                    return;
                }
            }

            display.setText(decimalFormat.format(result));
            expression.setText(""); // Clear the expression after calculation
            operator = "";
            isNewOperand = true;
        }
    }
}
