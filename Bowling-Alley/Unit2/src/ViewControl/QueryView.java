package ViewControl;

import persistence.SearchDb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static ViewControl.ViewUtils.*;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 */

public class QueryView extends JFrame implements ActionListener, ItemListener {

    private Container c;
    private JLabel queryFor;
    private JComboBox queryForInput;
    private JLabel queryUsing;
    private JComboBox queryUsingInput;
    private JLabel queryValue;
    private JTextField queryValueInput;

    private JButton sub, reset, queryPlayerScoreAvg, queryPlayerScoreMax, queryPlayerScoreMin, listAllBowlers;

    public QueryView() {
        setTitle("Query Window");
        setBounds(300, 90, 600, 600);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        queryPlayerScoreAvg = new JButton("Player Wise Average Score");
        setComponentProperties(queryPlayerScoreAvg, 10, 150, 20, 100, 100);
        queryPlayerScoreAvg.addActionListener(this);
        c.add(queryPlayerScoreAvg);

        queryPlayerScoreMax = new JButton("Player Wise Max Score");
        setComponentProperties(queryPlayerScoreMax, 10, 150, 20, 270, 100);
        queryPlayerScoreMax.addActionListener(this);
        c.add(queryPlayerScoreMax);

        queryPlayerScoreMin = new JButton("Player Wise Min Score");
        setComponentProperties(queryPlayerScoreMin, 10, 150, 20, 270, 50);
        queryPlayerScoreMin.addActionListener(this);
        c.add(queryPlayerScoreMin);

        listAllBowlers = new JButton("List All Bowlers");
        setComponentProperties(listAllBowlers, 10, 150, 20, 100, 50);
        listAllBowlers.addActionListener(this);
        c.add(listAllBowlers);

        queryFor = new JLabel("Query For: ");
        setComponentProperties(queryFor, 20, 200, 20, 100, 150);
        c.add(queryFor);

        Object[] tables = new String[0];
        Object[] cols = new String[0];
        try {
            tables = SearchDb.getTables();
            cols = SearchDb.getColumnsByTable(String.valueOf(tables[0]));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        queryForInput = createComboBox(250, 150, tables);
        queryForInput.addItemListener(this);
        c.add(queryForInput);

        queryUsing = new JLabel("Query Using: ");
        setComponentProperties(queryUsing, 20, 200, 20, 100, 200);
        c.add(queryUsing);

        queryUsingInput = createComboBox(250, 200, cols);
        c.add(queryUsingInput);

        queryValue = new JLabel("Query Value: ");
        setComponentProperties(queryValue, 20, 200, 20, 100, 250);
        c.add(queryValue);

        queryValueInput = new JTextField();
        setComponentProperties(queryValueInput, 15, 190, 20, 250, 250);
        c.add(queryValueInput);


        sub = new JButton("Submit");
        setComponentProperties(sub, 15, 100, 20, 150, 500);
        sub.addActionListener(this);
        c.add(sub);

        reset = new JButton("Reset");
        setComponentProperties(reset, 15, 100, 20, 270, 500);
        reset.addActionListener(this);
        c.add(reset);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource().equals(sub)) {
                System.out.println(queryForInput.getSelectedItem() + "," + queryUsingInput.getSelectedItem() + "," + queryValueInput.getText());
                java.util.List<Map<String, String>> result = null;

                result = SearchDb.getQueryResult(String.valueOf(queryForInput.getSelectedItem()),
                        String.valueOf(queryUsingInput.getSelectedItem()),
                        queryValueInput.getText());

                Object[] columns = SearchDb.getColumnsByTable(String.valueOf(queryForInput.getSelectedItem()));
                Object[][] data = parseMapToData(result, columns.length);
                QueryResultView queryResultView = new QueryResultView(columns, data);

            }
            if (e.getSource().equals(reset)) {
                queryForInput.setSelectedItem(queryForInput.getItemAt(0));
                queryValueInput.setText("");
            }
            if (e.getSource().equals(queryPlayerScoreAvg)) {
                List<Map<String, String>> result = SearchDb.getPlayerWiseScores("AVG");
                Object[] cols = new Object[]{"Name", "Average Score"};
                Object[][] data = parseMapToData(result, cols.length);
                QueryResultView queryResultView = new QueryResultView(cols, data);
            }
            if (e.getSource().equals(queryPlayerScoreMax)) {
                List<Map<String, String>> result = SearchDb.getPlayerWiseScores("MAX");
                Object[] cols = new Object[]{"Name", "Max Score"};
                Object[][] data = parseMapToData(result, cols.length);
                QueryResultView queryResultView = new QueryResultView(cols, data);
            }
            if (e.getSource().equals(queryPlayerScoreMin)) {
                List<Map<String, String>> result = SearchDb.getPlayerWiseScores("MIN");
                Object[] cols = new Object[]{"Name", "Min Score"};
                Object[][] data = parseMapToData(result, cols.length);
                QueryResultView queryResultView = new QueryResultView(cols, data);
            }
            if (e.getSource().equals(listAllBowlers)) {
                List<Map<String, String>> result = SearchDb.getAllBowlers();
                Object[] cols = SearchDb.getColumnsByTable(String.valueOf(queryForInput.getSelectedItem()));
                Object[][] data = parseMapToData(result, cols.length);
                QueryResultView queryResultView = new QueryResultView(cols, data);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (e.getSource().equals(queryForInput)) {
                System.out.println(e.getItem());
                Object[] cols;
                try {
                    cols = SearchDb.getColumnsByTable((String) e.getItem());
                    setItemsToComboBox(cols, queryUsingInput);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
