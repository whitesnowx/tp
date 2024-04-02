package staffconnect.logic.commands;

import org.junit.jupiter.api.BeforeEach;
import staffconnect.model.Model;
import staffconnect.model.ModelManager;
import staffconnect.model.UserPrefs;

import static staffconnect.testutil.TypicalPersons.getTypicalStaffBook;

public class RefreshCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalStaffBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getStaffBook(), new UserPrefs());
    }


}
