import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReqmanSharedModule } from 'app/shared';
import {
    RMProjectComponent,
    RMProjectDetailComponent,
    RMProjectUpdateComponent,
    RMProjectDeletePopupComponent,
    RMProjectDeleteDialogComponent,
    rMProjectRoute,
    rMProjectPopupRoute
} from './';

const ENTITY_STATES = [...rMProjectRoute, ...rMProjectPopupRoute];

@NgModule({
    imports: [ReqmanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RMProjectComponent,
        RMProjectDetailComponent,
        RMProjectUpdateComponent,
        RMProjectDeleteDialogComponent,
        RMProjectDeletePopupComponent
    ],
    entryComponents: [RMProjectComponent, RMProjectUpdateComponent, RMProjectDeleteDialogComponent, RMProjectDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReqmanRMProjectModule {}
