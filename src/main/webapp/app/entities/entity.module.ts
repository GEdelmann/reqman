import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ReqmanRMRequirementModule } from './rm-requirement/rm-requirement.module';
import { ReqmanRMTagModule } from './rm-tag/rm-tag.module';
import { ReqmanRMCategoryModule } from './rm-category/rm-category.module';
import { ReqmanRMPageModule } from './rm-page/rm-page.module';
import { ReqmanRMProjectModule } from './rm-project/rm-project.module';
import { ReqmanRMAttachementModule } from './rm-attachement/rm-attachement.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ReqmanRMRequirementModule,
        ReqmanRMTagModule,
        ReqmanRMCategoryModule,
        ReqmanRMPageModule,
        ReqmanRMProjectModule,
        ReqmanRMAttachementModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReqmanEntityModule {}
