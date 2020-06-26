package games.rednblack.editor.view.stage.tools.transformStrategy;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.utils.TransformCommandBuilder;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.editor.view.ui.followers.NormalSelectionFollower;

/**
 * Created by Sasun Poghosyan on 4/14/2016.
 */
public abstract class AbstractTransformStrategy implements ITransformStrategy {


    /**
     * on every anchor drag calculates width/height, x and y amounts.
     *
     * @param mouseDeltaX mouse delta on x axis
     * @param mouseDeltaY mouse delta on y axis
     * @param rotation    entity rotation. If you want to find vertical anchors drag value add 90 to {@param rotation}
     * @return array of three floats (new float[]{width/height, xComponent, yComponent};)
     */
    float[] calculateSizeAndXyAmount(float mouseDeltaX, float mouseDeltaY, float rotation) {
        float mouseDragAngle = MathUtils.atan2(mouseDeltaY, mouseDeltaX) * MathUtils.radDeg;
        float deltaA = rotation - mouseDragAngle;
        float c = (float) Math.sqrt(mouseDeltaX * mouseDeltaX + mouseDeltaY * mouseDeltaY);
        float a = c * MathUtils.cosDeg(deltaA);
        float xComponent = a * MathUtils.cosDeg(rotation);
        float yComponent = a * MathUtils.sinDeg(rotation);
        return new float[]{a, xComponent, yComponent};
    }

    void rotating(int anchor, TransformCommandBuilder transformCommandBuilder, Vector2 mousePointStage, float lastTransformAngle, float lastEntityAngle, TransformComponent transformComponent) {
        if (anchor >= NormalSelectionFollower.ROTATION_LT && anchor <= NormalSelectionFollower.ROTATION_LB) {
            Vector2 originPoint = new Vector2(transformComponent.x + transformComponent.originX, transformComponent.y + transformComponent.originY);
            mousePointStage.sub(originPoint);
            float currentAngle = mousePointStage.angle();
            float angleDiff = currentAngle - lastTransformAngle;
            float newRotation = lastEntityAngle + angleDiff;
            transformComponent.rotation = newRotation;
            transformCommandBuilder.setRotation(newRotation);
        }
    }

    void origin(float mouseDx, float mouseDy, int anchor, TransformComponent transformComponent) {
        if (anchor == NormalSelectionFollower.ORIGIN) {
            float newOriginX = transformComponent.originX;
            float newOriginY = transformComponent.originY;

            newOriginX = newOriginX + mouseDx * Sandbox.getInstance().getPixelPerWU();
            newOriginY = newOriginY + mouseDy * Sandbox.getInstance().getPixelPerWU();

            transformComponent.originX = newOriginX;
            transformComponent.originY = newOriginY;
        }
    }
}
