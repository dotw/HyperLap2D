/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package games.rednblack.editor.view.ui.box;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import games.rednblack.editor.view.ui.box.resourcespanel.*;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.editor.HyperLap2DFacade;
import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.h2d.common.view.ui.widget.imagetabbedpane.ImageTab;
import org.puremvc.java.interfaces.INotification;

import java.util.stream.Stream;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UIResourcesBoxMediator extends PanelMediator<UIResourcesBox> {

    private static final String TAG = UIResourcesBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private static final String PREFIX = "games.rednblack.editor.view.ui.box";

    public static final String IMAGE_RIGHT_CLICK = "IMAGE_RIGHT_CLICK";
    public static final String SPINE_ANIMATION_RIGHT_CLICK = "SPINE_ANIMATION_RIGHT_CLICK";
    public static final String SPRITE_ANIMATION_RIGHT_CLICK = "SPRITE_ANIMATION_RIGHT_CLICK";
    public static final String SPRITER_ANIMATION_RIGHT_CLICK = "SPRITER_ANIMATION_RIGHT_CLICK";
    public static final String LIBRARY_ITEM_RIGHT_CLICK = "LIBRARY_ITEM_RIGHT_CLICK";
    public static final String PARTICLE_EFFECT_RIGHT_CLICK = "PARTICLE_EFFECT_RIGHT_CLICK";

    public Array<DragAndDrop.Target> customTargets = new Array<DragAndDrop.Target>();

    @Override
    public void onRegister() {
        super.onRegister();
        facade = HyperLap2DFacade.getInstance();
        registerTabMediators();
        initTabs();
    }

    public UIResourcesBoxMediator() {
        super(NAME, new UIResourcesBox());
    }

    @Override
    public String[] listNotificationInterests() {
        String[] parentNotifications = super.listNotificationInterests();
        return Stream.of(parentNotifications, new String[]{
                ProjectManager.PROJECT_OPENED,
                ProjectManager.PROJECT_DATA_UPDATED,
                MsgAPI.ADD_TARGET,
            }).flatMap(Stream::of).toArray(String[]::new);
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:

                break;
            case ProjectManager.PROJECT_DATA_UPDATED:
                break;
            case MsgAPI.ADD_TARGET:
                customTargets.add(notification.getBody());
                break;
            default:
                break;
        }
    }

    private void registerTabMediators() {
        facade.registerMediator(new UIImagesTabMediator());
        facade.registerMediator(new UIAnimationsTabMediator());
        facade.registerMediator(new UILibraryItemsTabMediator());
        facade.registerMediator(new UIParticleEffectsTabMediator());
    }

    private void initTabs() {
        UIImagesTabMediator imagesTabMediator = facade.retrieveMediator(UIImagesTabMediator.NAME);
        ImageTab imagesTab = imagesTabMediator.getViewComponent();
        viewComponent.addTab(0, imagesTab);

        UIAnimationsTabMediator animationsTabMediator = facade.retrieveMediator(UIAnimationsTabMediator.NAME);
        ImageTab animationsTab = animationsTabMediator.getViewComponent();
        viewComponent.addTab(1, animationsTab);

        UILibraryItemsTabMediator libraryTabMediator = facade.retrieveMediator(UILibraryItemsTabMediator.NAME);
        ImageTab libraryItemsTab = libraryTabMediator.getViewComponent();
        viewComponent.addTab(2, libraryItemsTab);

        UIParticleEffectsTabMediator particlesTabMediator = facade.retrieveMediator(UIParticleEffectsTabMediator.NAME);
        ImageTab particlesTab = particlesTabMediator.getViewComponent();
        viewComponent.addTab(3, particlesTab);

        viewComponent.setActiveTabContent(imagesTab);
    }
}
