import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IRMAttachement } from 'app/shared/model/rm-attachement.model';

@Component({
    selector: '-rm-attachement-detail',
    templateUrl: './rm-attachement-detail.component.html'
})
export class RMAttachementDetailComponent implements OnInit {
    rMAttachement: IRMAttachement;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMAttachement }) => {
            this.rMAttachement = rMAttachement;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
