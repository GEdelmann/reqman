import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReqmanSharedModule } from 'app/shared';
import {
    RMCategoryComponent,
    RMCategoryDetailComponent,
    RMCategoryUpdateComponent,
    RMCategoryDeletePopupComponent,
    RMCategoryDeleteDialogComponent,
    rMCategoryRoute,
    rMCategoryPopupRoute
} from './';

const ENTITY_STATES = [...rMCategoryRoute, ...rMCategoryPopupRoute];

@NgModule({
    imports: [ReqmanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RMCategoryComponent,
        RMCategoryDetailComponent,
        RMCategoryUpdateComponent,
        RMCategoryDeleteDialogComponent,
        RMCategoryDeletePopupComponent
    ],
    entryComponents: [RMCategoryComponent, RMCategoryUpdateComponent, RMCategoryDeleteDialogComponent, RMCategoryDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReqmanRMCategoryModule {}
