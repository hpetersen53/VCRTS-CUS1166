package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IncomingJobs {
    private JFrame jframe;
    private JPanel jPanel;
    private JScrollPane jScrollPane;
    private List<JButton> jobs;

    public IncomingJobs() {
        jframe = new JFrame("Incoming Job Requests");
        jframe.setSize(300,300);

        jobs = getAllJobsAsButtons();

        jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        for(JButton job : jobs) {
            job.addActionListener(e -> {
                int result = JOptionPane.showConfirmDialog(null, "Just Do It");

                if(result == JOptionPane.YES_NO_OPTION){
                    System.out.println("Accepted");
                    removeJob(job);
                }else{
                    System.out.println("rejected");
                }
            });
        }
        for(int i = 0; i < jobs.size(); i++) {
            jPanel.add(jobs.get(i));
        }

        jScrollPane = new JScrollPane(jPanel);

        jframe.add(jScrollPane);

        jframe.setVisible(true);
        jframe.setLocationRelativeTo(null);
    }

    public List<JButton> getAllJobsAsButtons(){
        List<JButton> jobs = new ArrayList<>();
        jobs.add(new JButton("Req 1"));
        jobs.add(new JButton("Req 2"));
        jobs.add(new JButton("Req 3"));
        jobs.add(new JButton("Req 4"));
        jobs.add(new JButton("Req 5"));


        for(JButton job : jobs)
            job.setMaximumSize(new Dimension(250,50));

        return jobs;
    }
    public void removeJob(JButton job){
        if(jobs.contains(job)) {
            jobs.remove(job);
            jPanel.remove(job);

            jPanel.revalidate();
            jPanel.repaint();
        }
    }
}
