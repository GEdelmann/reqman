import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IRMCategory } from 'app/shared/model/rm-category.model';
import { RMCategoryService } from './rm-category.service';
import { IRMRequirement } from 'app/shared/model/rm-requirement.model';
import { RMRequirementService } from 'app/entities/rm-requirement';

@Component({
    selector: '-rm-category-update',
    templateUrl: './rm-category-update.component.html'
})
export class RMCategoryUpdateComponent implements OnInit {
    private _rMCategory: IRMCategory;
    isSaving: boolean;

    rmrequirements: IRMRequirement[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private rMCategoryService: RMCategoryService,
        private rMRequirementService: RMRequirementService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rMCategory }) => {
            this.rMCategory = rMCategory;
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
        if (this.rMCategory.id !== undefined) {
            this.subscribeToSaveResponse(this.rMCategoryService.update(this.rMCategory));
        } else {
            this.subscribeToSaveResponse(this.rMCategoryService.create(this.rMCategory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRMCategory>>) {
        result.subscribe((res: HttpResponse<IRMCategory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get rMCategory() {
        return this._rMCategory;
    }

    set rMCategory(rMCategory: IRMCategory) {
        this._rMCategory = rMCategory;
    }
}
