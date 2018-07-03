import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReqmanSharedModule } from 'app/shared';
import {
    RMTagComponent,
    RMTagDetailComponent,
    RMTagUpdateComponent,
    RMTagDeletePopupComponent,
    RMTagDeleteDialogComponent,
    rMTagRoute,
    rMTagPopupRoute
} from './';

const ENTITY_STATES = [...rMTagRoute, ...rMTagPopupRoute];

@NgModule({
    imports: [ReqmanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [RMTagComponent, RMTagDetailComponent, RMTagUpdateComponent, RMTagDeleteDialogComponent, RMTagDeletePopupComponent],
    entryComponents: [RMTagComponent, RMTagUpdateComponent, RMTagDeleteDialogComponent, RMTagDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReqmanRMTagModule {}
