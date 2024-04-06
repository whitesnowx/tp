package staffconnect.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import staffconnect.model.meeting.Meeting;

/**
 * A UI component that displays information of a {@code Meetings} withing a person.
 */
public class MeetingsCard extends UiPart<Region> {

    private static final String FXML = "MeetingsListCard.fxml";
    private double pixelHeight = 0;
    private double pixelWidth = 0;
    @FXML
    private Label id;
    @FXML
    private Label description;
    @FXML
    private Label date;

    /**
     * Creates a {@code MeetingCode} with the given {@code Meeting} and index to display.
     */
    public MeetingsCard(Meeting meeting, int index) {

        super(FXML);
        id.setText(index + ".  ");
        description.setText(meeting.getDescription().toString());
        date.setText(meeting.getStartDate().toString());
        computeSystemSize(description.getStyle());
    }

    private void computeSystemSize(String style) {
        /*
         There is no reliable way to compute to the accurate pixel height for different systems screens.
         Hence, this is the devastation of trying to accommodate to different resolutions and screen sizes.
         */
        Text helper = new Text();
        helper.setStyle(style);
        helper.setLineSpacing(0);
        char[] testFont = new char[]{'a', 'A', '/', '1', '-', ' ', '|', 'P', '%', '@'};
        double maxWidth = 0;
        double maxHeight = 0;
        for (char c : testFont) {
            String input = c + "";
            helper.setText(input);
            double currentWidth = helper.getLayoutBounds().getWidth();
            double currentHeight = helper.getLayoutBounds().getHeight();
            if (currentWidth > maxWidth) {
                maxWidth = currentWidth;
            }
            if (currentHeight > maxHeight) {
                maxHeight = currentHeight;
            }
        }
        pixelHeight = maxHeight;
        pixelWidth = maxWidth;
    }

    public double getWidth() {
        return pixelWidth;
    }

    public double getHeight() {
        return pixelHeight;
    }

}
