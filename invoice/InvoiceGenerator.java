import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceGenerator {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField nameField, itemField, qtyField, priceField;
    private JButton addButton, saveButton, showButton, searchButton;
    private float total = 0;
    private StringBuilder invoiceContent = new StringBuilder();

    public InvoiceGenerator() {
        frame = new JFrame("MR. CAFE - Invoice Generator");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        textArea = new JTextArea(20, 40);
        textArea.setEditable(false);

        nameField = new JTextField(20);
        itemField = new JTextField(15);
        qtyField = new JTextField(5);
        priceField = new JTextField(5);

        addButton = new JButton("Add Item");
        saveButton = new JButton("Save Invoice");
        showButton = new JButton("Show Invoices");
        searchButton = new JButton("Search Invoice");

        frame.add(new JLabel("Customer Name:"));
        frame.add(nameField);
        frame.add(new JLabel("Item:"));
        frame.add(itemField);
        frame.add(new JLabel("Qty:"));
        frame.add(qtyField);
        frame.add(new JLabel("Price:"));
        frame.add(priceField);
        frame.add(addButton);
        frame.add(saveButton);
        frame.add(showButton);
        frame.add(searchButton);
        frame.add(new JScrollPane(textArea));

        addButton.addActionListener(e -> addItem());
        saveButton.addActionListener(e -> saveInvoice());
        showButton.addActionListener(e -> showInvoices());
        searchButton.addActionListener(e -> searchInvoice());

        frame.setVisible(true);
    }

    private void addItem() {
        String item = itemField.getText();
        int qty = Integer.parseInt(qtyField.getText());
        float price = Float.parseFloat(priceField.getText());
        float totalItem = qty * price;
        total += totalItem;

        invoiceContent.append(item).append("\t").append(qty).append("\t").append(totalItem).append("\n");
        textArea.setText(invoiceContent.toString());
    }

    private void saveInvoice() {
        String customer = nameField.getText();
        String date = new SimpleDateFormat("dd MMM, yyyy").format(new Date());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("RestaurantBill.txt", true))) {
            writer.write("Customer: " + customer + "\n");
            writer.write("Date: " + date + "\n");
            writer.write("Items\tQty\tTotal\n");
            writer.write(invoiceContent.toString());
            writer.write("---------------------------------------\n");
            writer.write("Grand Total:\t" + total + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(frame, "Invoice Saved Successfully!");
    }

    private void showInvoices() {
        try (BufferedReader reader = new BufferedReader(new FileReader("RestaurantBill.txt"))) {
            textArea.read(reader, null);
        } catch (IOException e) {
            textArea.setText("No invoices found.");
        }
    }

    private void searchInvoice() {
        String searchName = JOptionPane.showInputDialog(frame, "Enter customer name:");
        StringBuilder result = new StringBuilder();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("RestaurantBill.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Customer: " + searchName)) {
                    found = true;
                    result.append(line).append("\n");
                    while ((line = reader.readLine()) != null && !line.equals("")) {
                        result.append(line).append("\n");
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        textArea.setText(found ? result.toString() : "Invoice not found.");
    }

    public static void main(String[] args) {
        new InvoiceGenerator();
    }
}
