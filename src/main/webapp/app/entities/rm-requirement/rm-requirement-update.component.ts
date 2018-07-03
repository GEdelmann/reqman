import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IRMRequirement } from 'app/shared/model/rm-requirement.model';
import { RMRequirementService } from './rm-requirement.service';
import { IRMProject } from 'app/shared/model/rm-project.model';
import { RMProjectService } from 'app/entities/rm-project';
import { IRMTag } from 'app/shared/model/rm-tag.model';
import { RMTagService } from 'app/entities/rm-tag';
import { IRMCategory } from 'app/shared/model/rm-category.model';
import { RMCategoryService } from 'app/entities/rm-category';

@Component({
    selector: '-rm-requirement-update',
    templateUrl: './rm-requirement-update.component.html'
})
export class RMRequirementUpdateComponent implements OnInit {
    private _rMRequirement: IRMRequirement;
    isSaving: boolean;

    rmprojects: IRMProject[];

    rmtags: IRMTag[];

    rmcategories: IRMCategory[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private rMRequirementService: RMRequirementService,
        private rMProjectService: RMProjectService,
        private rMTagService: RMTagService,
        private rMCategoryService: RMCategoryService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rMRequirement }) => {
            this.rMRequirement = rMRequirement;
        });
        this.rMProjectService.query().subscribe(
            (res: HttpResponse<IRMProject[]>) => {
                this.rmprojects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.rMTagService.query().subscribe(
            (res: HttpResponse<IRMTag[]>) => {
                this.rmtags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.rMCategoryService.query().subscribe(
            (res: HttpResponse<IRMCategory[]>) => {
                this.rmcategories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.rMRequirement.id !== undefined) {
            this.subscribeToSaveResponse(this.rMRequirementService.update(this.rMRequirement));
        } else {
            this.subscribeToSaveResponse(this.rMRequirementService.create(this.rMRequirement));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRMRequirement>>) {
        result.subscribe((res: HttpResponse<IRMRequirement>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackRMProjectById(index: number, item: IRMProject) {
        return item.id;
    }

    trackRMTagById(index: number, item: IRMTag) {
        return item.id;
    }

    trackRMCategoryById(index: number, item: IRMCategory) {
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
    get rMRequirement() {
        return this._rMRequirement;
    }

    set rMRequirement(rMRequirement: IRMRequirement) {
        this._rMRequirement = rMRequirement;
    }
}
