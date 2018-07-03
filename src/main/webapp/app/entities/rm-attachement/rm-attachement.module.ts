import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReqmanSharedModule } from 'app/shared';
import {
    RMAttachementComponent,
    RMAttachementDetailComponent,
    RMAttachementUpdateComponent,
    RMAttachementDeletePopupComponent,
    RMAttachementDeleteDialogComponent,
    rMAttachementRoute,
    rMAttachementPopupRoute
} from './';

const ENTITY_STATES = [...rMAttachementRoute, ...rMAttachementPopupRoute];

@NgModule({
    imports: [ReqmanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RMAttachementComponent,
        RMAttachementDetailComponent,
        RMAttachementUpdateComponent,
        RMAttachementDeleteDialogComponent,
        RMAttachementDeletePopupComponent
    ],
    entryComponents: [
        RMAttachementComponent,
        RMAttachementUpdateComponent,
        RMAttachementDeleteDialogComponent,
        RMAttachementDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReqmanRMAttachementModule {}
