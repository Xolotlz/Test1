package c.mike.controldegastos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Grafica extends AppCompatActivity {

    int get_id,get_month,get_year;
    PieChart pieChart;
    PieChart pieChart2;
    PieData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grafica);
        TextView etSEV = (TextView) findViewById(R.id.etSueldoExtraValue);
        GetInfo();

        final Spinner dropMonth = (Spinner) findViewById(R.id.idDropMonth);
        final Spinner dropYear = (Spinner) findViewById(R.id.idDropYear);
        Button selecter = (Button) findViewById(R.id.idSelector);
        pieChart = (PieChart) findViewById(R.id.idPie);

        dropMonth.setSelection((get_month-1));
        dropYear.setSelection((get_year-2018));

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                e.getY()    (yxData pos 0)  monto
//                h.getY()    (yxData pos 1)  id_cat
//                Toast.makeText(Grafica.this, "Monto: "+e.getY()+"    Categoria: "+h.getX(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected() {
            }
        });
        selecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mm = dropMonth.getSelectedItemPosition();
                String y = dropYear.getSelectedItem().toString();
                int yyyy = Integer.parseInt(y);
                mm = (mm + 1);
                pie(mm,yyyy);
            }
        });
        selecter.performClick();
    }

    private void pie(int mm, int yyyy){

//        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDragDecelerationFrictionCoef(0.96f);
        pieChart.setHoleRadius(25f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(35f);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        pieChart.setDrawEntryLabels(false);
//        pieChart.setTouchEnabled(false);

        Legend l = pieChart.getLegend();
        l.setFormSize(15f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        List<Integer> yyData = this.datos(mm,yyyy);
        String[] xData = {"Ingreso Extra", "Transporte" , "Alimentos" , "Accesorios", "Otros"};
        ArrayList<PieEntry> yxData = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            yxData.add(new PieEntry(yyData.get(i),xData[i]));
        }
        PieDataSet dataSet = new PieDataSet(yxData,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(27f);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(255,78,0));
        colors.add(Color.rgb(27,230,0));
        colors.add(Color.rgb(27,38,255));
        colors.add(Color.rgb(247,77,255));
        colors.add(Color.rgb(165,64,3));
        dataSet.setColors(colors);

        data = new PieData((dataSet));
        data.setValueTextSize(12f);
        pieChart.setData(data);

        ArrayList<Integer> colors2 = new ArrayList<>();

        int sueldo = getSueldoTotal(mm,yyyy);
        int gastado = 0;

        for(int i = 1; i < 5; i++) {
            gastado = gastado + (yyData.get(i));
        }
        int res = (sueldo+(yyData.get(0))) - gastado;
        int deuda = gastado - (sueldo+(yyData.get(0)));

        TextView etSEV = (TextView) findViewById(R.id.etSueldoExtraValue);
        TextView etGD = (TextView) findViewById(R.id.etGastoDeuda);
        TextView etGS = (TextView) findViewById(R.id.etGastoSueldo);

        etSEV.setText(formatValue(sueldo+(yyData.get(0))));

        String xData2[] = new String[2];
        int[] yData2 = new int[2];

        if(res > 0){
            xData2[0]="Gastado";
            xData2[1]="Sobra";
            yData2[0] = gastado;
            yData2[1] = res;
            colors2.add(Color.LTGRAY);
            colors2.add(Color.rgb(255,190,0));
            etGD.setText(formatValue(gastado));
            etGS.setText(formatValue(res));
        }else{
            xData2[0]="Deuda";
            xData2[1]="Gastado";
            yData2[0] = deuda;
            yData2[1] = 1000000;
            colors2.add(Color.RED);
            colors2.add(Color.LTGRAY);
            etGD.setText(formatValue(deuda));
            etGS.setText(formatValue((sueldo+(yyData.get(0)))+deuda));
        }

        ArrayList<Integer> yyData2 = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            yyData2.add(yData2[i]);
        }

        ArrayList<PieEntry> yxData2 = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            yxData2.add(new PieEntry(yyData2.get(i),xData2[i]));
        }

        pieChart2 = (PieChart) findViewById(R.id.idPie2);
        pieChart2.getDescription().setEnabled(false);
        pieChart2.setTouchEnabled(false);
        pieChart2.setHoleRadius(84);
        pieChart2.setHoleColor(Color.TRANSPARENT);
        pieChart2.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        pieChart2.setDrawEntryLabels(false);

//        pieChart2.setCenterTextRadiusPercent(100);
//        pieChart2.setCenterTextColor(Color.BLACK);
//        pieChart2.setCenterTextSize(10);
//        pieChart2.setCenterText("=D");

        Legend l2 = pieChart2.getLegend();
        l2.setFormSize(15f);
        l2.setForm(Legend.LegendForm.CIRCLE);
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l2.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        l2.setOrientation(Legend.LegendOrientation.VERTICAL);
        l2.setDrawInside(false);

        PieDataSet dataSet2 = new PieDataSet(yxData2,"");

        dataSet2.setColors(colors2);
        dataSet2.setSelectionShift(-1);

        PieData data2 = new PieData((dataSet2));
        data2.setValueTextSize(0f);

        pieChart2.setData(data2);
    }

    public static String formatValue(double value) {
        return new DecimalFormat("$###,###,###").format(value);
    }

    private int getSueldoTotal(int mm, int yyyy){
        DBCreate DBClass = new DBCreate(this);
        SQLiteDatabase db = DBClass.getReadableDatabase();
        int monto = 0;
        Cursor fila;
        fila = db.rawQuery("select monto from Detalle where " +
                "id_user = "+get_id+" and " +
                "id_cat="+1+" and " +
                "id_month="+mm+" and " +
                "id_year="+yyyy,null);
        if(fila.moveToFirst()==true) {
            monto = fila.getInt(0);
        }
        db.close();
        return monto;
    }

    private ArrayList<Integer> datos(int mm,int yyyy){
        DBCreate DBClass = new DBCreate(this);
        SQLiteDatabase db = DBClass.getReadableDatabase();
        ArrayList<Integer> yyData = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            int monto;
            Cursor fila;
            fila = db.rawQuery("select total from Resultado where " +
                    "id_user = "+get_id+" and " +
                    "id_cat="+(i+2)+" and " +
                    "id_month="+mm+" and " +
                    "id_year="+yyyy,null);
            if(fila.moveToFirst()==true) {
                monto = fila.getInt(0);
                yyData.add(monto);

            }else{
                yyData.add(0);
                yyData.add(0);
                yyData.add(0);
                yyData.add(0);
                yyData.add(0);
                i = 6;
            }
        }
        db.close();
        return(yyData);
    }

    private void GetInfo(){
        Bundle extras = getIntent().getExtras();
        get_id = extras.getInt("id");
        get_month = extras.getInt("month");
        get_year = extras.getInt("year");
    }
}
