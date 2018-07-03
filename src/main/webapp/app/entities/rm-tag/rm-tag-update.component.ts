import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IRMTag } from 'app/shared/model/rm-tag.model';
import { RMTagService } from './rm-tag.service';
import { IRMRequirement } from 'app/shared/model/rm-requirement.model';
import { RMRequirementService } from 'app/entities/rm-requirement';

@Component({
    selector: '-rm-tag-update',
    templateUrl: './rm-tag-update.component.html'
})
export class RMTagUpdateComponent implements OnInit {
    private _rMTag: IRMTag;
    isSaving: boolean;

    rmrequirements: IRMRequirement[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private rMTagService: RMTagService,
        private rMRequirementService: RMRequirementService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rMTag }) => {
            this.rMTag = rMTag;
        });
        this.rMRequirementService.query().subscribe(
            (res: HttpResponse<IRMRequirement[]>) => {
                this.rmrequirements = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.rMTag.id !== undefined) {
            this.subscribeToSaveResponse(this.rMTagService.update(this.rMTag));
        } else {
            this.subscribeToSaveResponse(this.rMTagService.create(this.rMTag));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRMTag>>) {
        result.subscribe((res: HttpResponse<IRMTag>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get rMTag() {
        return this._rMTag;
    }

    set rMTag(rMTag: IRMTag) {
        this._rMTag = rMTag;
    }
}
