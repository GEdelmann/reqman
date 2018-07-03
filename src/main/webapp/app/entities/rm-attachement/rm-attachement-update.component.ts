import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IRMAttachement } from 'app/shared/model/rm-attachement.model';
import { RMAttachementService } from './rm-attachement.service';
import { IRMRequirement } from 'app/shared/model/rm-requirement.model';
import { RMRequirementService } from 'app/entities/rm-requirement';
import { IRMPage } from 'app/shared/model/rm-page.model';
import { RMPageService } from 'app/entities/rm-page';

@Component({
    selector: '-rm-attachement-update',
    templateUrl: './rm-attachement-update.component.html'
})
export class RMAttachementUpdateComponent implements OnInit {
    private _rMAttachement: IRMAttachement;
    isSaving: boolean;

    rmrequirements: IRMRequirement[];

    rmpages: IRMPage[];

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private rMAttachementService: RMAttachementService,
        private rMRequirementService: RMRequirementService,
        private rMPageService: RMPageService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rMAttachement }) => {
            this.rMAttachement = rMAttachement;
        });
        this.rMRequirementService.query().subscribe(
            (res: HttpResponse<IRMRequirement[]>) => {
                this.rmrequirements = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.rMPageService.query().subscribe(
            (res: HttpResponse<IRMPage[]>) => {
                this.rmpages = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.rMAttachement.id !== undefined) {
            this.subscribeToSaveResponse(this.rMAttachementService.update(this.rMAttachement));
        } else {
            this.subscribeToSaveResponse(this.rMAttachementService.create(this.rMAttachement));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRMAttachement>>) {
        result.subscribe((res: HttpResponse<IRMAttachement>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackRMRequirementById(index: number, item: IRMRequirement) {
        return item.id;
    }

    trackRMPageById(index: number, item: IRMPage) {
        return item.id;
    }
    get rMAttachement() {
        return this._rMAttachement;
    }

    set rMAttachement(rMAttachement: IRMAttachement) {
        this._rMAttachement = rMAttachement;
    }
}
