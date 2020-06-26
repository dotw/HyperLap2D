package games.rednblack.editor.view.ui.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import games.rednblack.editor.HyperLap2DFacade;
import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.renderer.data.SceneVO;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.h2d.common.H2DDialog;

public class SaveDocumentDialog extends H2DDialog {

    private VisLabel messageLabel;

    public SaveDocumentDialog() {
        super("Save Document");

        messageLabel = new VisLabel();
        getContentTable().pad(10);
        getContentTable().add(messageLabel);
        VisTextButton yesButton = new VisTextButton("Yes");
        VisTextButton noButton = new VisTextButton("No");
        VisTextButton cancelButton = new VisTextButton("Cancel");

        getButtonsTable().add(cancelButton).width(93).pad(8).padLeft(16);
        getButtonsTable().add(noButton).width(93).pad(8);
        getButtonsTable().add(yesButton).width(93).pad(8).padRight(16);

        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sandbox sandbox = Sandbox.getInstance();
                HyperLap2DFacade facade = HyperLap2DFacade.getInstance();
                ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
                SceneVO vo = sandbox.sceneVoFromItems();
                projectManager.saveCurrentProject(vo);

                Gdx.app.exit();
            }
        });

        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SaveDocumentDialog.this.close();
            }
        });
    }

    public void updateMessage(String projectTitle) {
        messageLabel.setText("Save changes to '" + projectTitle + "' before exit?");
    }
}
