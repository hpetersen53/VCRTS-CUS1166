package gui;

import main.Job;
import main.VCController;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class jobAcknowledgment {

    private JFrame frame;
    private JPanel panel;
    private JScrollPane scrollPane;
    private List<JButton> jobButtons;
    private List<String> jobDetails;

    public jobAcknowledgment() {
        frame = new JFrame("Job Acknowledgment");
        frame.setSize(300, 300);

        jobButtons = new ArrayList<>();
        jobDetails = readAcknowledgedJobs();

        if (!jobDetails.isEmpty()) {
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            for (JButton jobButton : jobButtons) {
                jobButton.addActionListener(e -> {
                    String detail = jobDetails.get(jobButtons.indexOf(jobButton));
                    JOptionPane.showMessageDialog(frame, detail, "Job Details", JOptionPane.INFORMATION_MESSAGE);

                    int result = JOptionPane.showConfirmDialog(frame, "Click OK to remove this job.", "Confirm", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        removeJob(jobButton, detail);
                    }
                });
                panel.add(jobButton);
            }

            scrollPane = new JScrollPane(panel);
            frame.add(scrollPane);

        } else {
            JOptionPane.showMessageDialog(null, "No jobs currently acknowledged.");
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();

        });

        frame.add(backButton, BorderLayout.SOUTH);

        frame.setVisible(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        
        int xPosition = screenWidth - frame.getWidth();  
        int yPosition = (screenHeight - frame.getHeight()) / 2;  
        frame.setLocation(xPosition-250, yPosition);

    }

    public static void saveacceptedJobData(Job job) {
        String fileName = "JobAcknowledgment.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Get the current timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            // Append job details to the file
            writer.write("This Job has been accepted, details below\n");
            writer.write("Timestamp: " + timestamp + "\n");
            writer.write("Client ID: " + job.getClientID() + "\n");
            writer.write("Level of Redundancy: " + job.getLevelOfRedundancy() + "\n");
            writer.write("Job Duration: " + job.getJobDuration() + "\n");
            writer.write("Payout: " + job.getPayout() + "\n");
            writer.write("Title: " + job.getTitle() + "\n");
            writer.write("Deadline: " + job.getDeadline() + "\n");
            writer.write("FileName: " + (job.getAttachedFileName() != null ? job.getAttachedFileName() : "N/A") + "\n");
            writer.newLine(); // Add an empty line between jobs
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }

    public static void saverejectectedJobData(Job job) {
        String fileName = "JobAcknowledgment.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Get the current timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            // Append job details to the file
            writer.write("This Job has been rejected, details below\n");
            writer.write("Timestamp: " + timestamp + "\n");
            writer.write("Client ID: " + job.getClientID() + "\n");
            writer.write("Level of Redundancy: " + job.getLevelOfRedundancy() + "\n");
            writer.write("Job Duration: " + job.getJobDuration() + "\n");
            writer.write("Payout: " + job.getPayout() + "\n");
            writer.write("Title: " + job.getTitle() + "\n");
            writer.write("Deadline: " + job.getDeadline() + "\n");
            writer.write("FileName: " + (job.getAttachedFileName() != null ? job.getAttachedFileName() : "N/A") + "\n");
            writer.newLine(); // Add an empty line between jobs
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }

    private List<String> readAcknowledgedJobs() {
        List<String> jobDetails = new ArrayList<>();
        String fileName = "JobAcknowledgment.txt";

        String line;
        String details = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    details += line + "\n";
                } else if (!details.isEmpty()) {
                    jobButtons.add(new JButton("Client ID: " + extractClientID(details)));
                    jobDetails.add(details.trim());
                    details = "";
                }
            }

            if (!details.isEmpty()) { // Handle the last job entry
                jobButtons.add(new JButton("Client ID: " + extractClientID(details)));
                jobDetails.add(details.trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jobDetails;
    }

    private String extractClientID(String details) {
        for (String line : details.split("\n")) {
            if (line.startsWith("Client ID:")) {
                return line.substring(line.indexOf(":") + 1).trim();
            }
        }
        return "Unknown";
    }

    private void removeJob(JButton jobButton, String detail) {
        if (jobButtons.contains(jobButton) && jobDetails.contains(detail)) {
            jobButtons.remove(jobButton);
            jobDetails.remove(detail);
            panel.remove(jobButton);

            rewriteAcknowledgmentFile();

            panel.revalidate();
            panel.repaint();
        }

        if (jobButtons.isEmpty()) {
            frame.dispose();
            JOptionPane.showMessageDialog(null, "No jobs currently acknowledged.");

        }
    }

    private void rewriteAcknowledgmentFile() {
        String fileName = "JobAcknowledgment.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String detail : jobDetails) {
                writer.write(detail);
                writer.newLine();
                writer.newLine(); // Ensure spacing between entries
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
