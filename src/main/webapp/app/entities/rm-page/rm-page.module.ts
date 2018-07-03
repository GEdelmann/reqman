import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReqmanSharedModule } from 'app/shared';
import {
    RMPageComponent,
    RMPageDetailComponent,
    RMPageUpdateComponent,
    RMPageDeletePopupComponent,
    RMPageDeleteDialogComponent,
    rMPageRoute,
    rMPagePopupRoute
} from './';

const ENTITY_STATES = [...rMPageRoute, ...rMPagePopupRoute];

@NgModule({
    imports: [ReqmanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [RMPageComponent, RMPageDetailComponent, RMPageUpdateComponent, RMPageDeleteDialogComponent, RMPageDeletePopupComponent],
    entryComponents: [RMPageComponent, RMPageUpdateComponent, RMPageDeleteDialogComponent, RMPageDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReqmanRMPageModule {}
