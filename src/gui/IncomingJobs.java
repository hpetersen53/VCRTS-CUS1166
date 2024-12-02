package gui;

import main.*;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class IncomingJobs {
    private JFrame jframe;
    private JPanel jPanel;
    private JScrollPane jScrollPane;
    private List<JButton> jobs;
    private List<String> jobDetails;

    public IncomingJobs() {
        jframe = new JFrame("Incoming Job Requests");
        jframe.setSize(300,300);

        jobs = new ArrayList<>();
        jobDetails = readJobs();

        if(!jobDetails.isEmpty()) {
            resizeButtons();

            jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

            for (JButton job : jobs) {
                job.addActionListener(e -> {
                    String detail = jobDetails.get(jobs.indexOf(job));
                    int result = JOptionPane.showConfirmDialog(null, detail);

                    if (result == JOptionPane.YES_NO_OPTION) {
                        System.out.println("Accepted");
                        Job objJob = addToJobList(detail);
                        ControllerDashboard.saveJobData(objJob);
                        removeJob(job, detail);
                    } else if (result == JOptionPane.NO_OPTION) {
                        System.out.println("Rejected");
                        removeJob(job, detail);
                    } else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                        System.out.println("Cancelled");
                        jframe.dispose();
                        IncomingJobs IncomingJobs = new IncomingJobs();
                        // Reload the IncomingJobs frame
                    }
                });
            }
            JButton backButton = new JButton("Back");
            backButton.setMaximumSize(new Dimension(80, 30));
            backButton.addActionListener(e -> {
                jframe.dispose(); // Close current frame
                VCController controller = new VCController();
                new ControllerDashboard(controller); // Navigate to the previous screen
            });

            jframe.add(backButton, BorderLayout.SOUTH);



            for (int i = 0; i < jobs.size(); i++) {
                jPanel.add(jobs.get(i));
            }

            jScrollPane = new JScrollPane(jPanel);

            jframe.add(jScrollPane);

            jframe.setVisible(true);
            jframe.setLocationRelativeTo(null);
        }else{
            JOptionPane.showMessageDialog(null, "No job requests currently");
            VCController controller = new VCController();
            new ControllerDashboard(controller);
        }
    }

    private Job addToJobList(String detail) {
        String [] jobDetail = detail.split("\n");
        List<String> jobObject = new ArrayList<>();
        for(String s : jobDetail){
            String [] smallPart = s.split(":");
            jobObject.add(smallPart[1].trim());
        }
        Job job = new Job(Integer.parseInt(jobObject.get(0)), Integer.parseInt(jobObject.get(1)), 0, Integer.parseInt(jobObject.get(2)),
                Double.parseDouble(jobObject.get(4)), jobObject.get(3), LocalDate.parse(jobObject.get(5)), jobObject.get(6));
        
        // Sends the new job to the database
        Database.insertJob(job);
        
        // Old version before JobID added
//        Job job = new Job(Integer.parseInt(jobObject.get(0)), 0, Integer.parseInt(jobObject.get(1)),
//                Double.parseDouble(jobObject.get(3)), jobObject.get(2), LocalDate.parse(jobObject.get(4)), jobObject.get(5));

        return job;
    }

    public void resizeButtons(){
        for(JButton job : jobs)
            job.setMaximumSize(new Dimension(250,50));
    }
    public void removeJob(JButton job, String detail){
        if(jobs.contains(job) && jobDetails.contains(detail)) {
            jobs.remove(job);
            jobDetails.remove(detail);
            jPanel.remove(job);

            if(jobs.isEmpty()){
                VCController controller = new VCController();
                new ControllerDashboard(controller);
                jframe.dispose();
            }

            rewriteJobFile();

            jPanel.revalidate();
            jPanel.repaint();
        }
    }

    private void rewriteJobFile(){
        String fileName = "IncomingJobs.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String jobDetail : jobDetails) {
                Job job = addToJobList(jobDetail);
                writer.write(job.toFileString());
                writer.newLine();  // Ensure each job is written on a new line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<String> readJobs() {
        List<String> jobDetails = new ArrayList<>();
        String fileName = "IncomingJobs.txt";

        String line;
        String details;
        int jobId = 0;
        int clientId = 0;
        String title = "";
        int JobDuration = 0;
        double payout = 0.0;
        LocalDate deadline = null;
        String attachedFileName = "None";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Job ID:" )) {
                	jobId = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                }
                else if (line.startsWith("Client ID:")) {
                    clientId = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Job Duration:")) {
                    JobDuration = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Title:")) {
                    title = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Payout:")) {
                    payout = Double.parseDouble(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Deadline:")) {
                    deadline = LocalDate.parse(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("FileName:")) {
                    attachedFileName = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.isEmpty()) {
                    jobs.add(new JButton("Client ID:"+ clientId));
                    details = "Client ID: " + clientId + "\n Job Duration: " + JobDuration + "\n Title: " + title + "\n Payout: " + payout + "\n Deadline: " + deadline
                            + "\n FileName: " + attachedFileName;
                    jobDetails.add(details);
                }
            }
        } catch (DateTimeParseException e) {
            //System.out.println("Error parsing deadline for job: " + line);
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
        return jobDetails;
    }
}