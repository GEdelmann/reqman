import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReqmanSharedModule } from 'app/shared';
import {
    RMRequirementComponent,
    RMRequirementDetailComponent,
    RMRequirementUpdateComponent,
    RMRequirementDeletePopupComponent,
    RMRequirementDeleteDialogComponent,
    rMRequirementRoute,
    rMRequirementPopupRoute
} from './';

const ENTITY_STATES = [...rMRequirementRoute, ...rMRequirementPopupRoute];

@NgModule({
    imports: [ReqmanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RMRequirementComponent,
        RMRequirementDetailComponent,
        RMRequirementUpdateComponent,
        RMRequirementDeleteDialogComponent,
        RMRequirementDeletePopupComponent
    ],
    entryComponents: [
        RMRequirementComponent,
        RMRequirementUpdateComponent,
        RMRequirementDeleteDialogComponent,
        RMRequirementDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReqmanRMRequirementModule {}
