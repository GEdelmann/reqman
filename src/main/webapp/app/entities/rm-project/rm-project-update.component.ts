import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IRMProject } from 'app/shared/model/rm-project.model';
import { RMProjectService } from './rm-project.service';

@Component({
    selector: '-rm-project-update',
    templateUrl: './rm-project-update.component.html'
})
export class RMProjectUpdateComponent implements OnInit {
    private _rMProject: IRMProject;
    isSaving: boolean;

    constructor(private rMProjectService: RMProjectService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rMProject }) => {
            this.rMProject = rMProject;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.rMProject.id !== undefined) {
            this.subscribeToSaveResponse(this.rMProjectService.update(this.rMProject));
        } else {
            this.subscribeToSaveResponse(this.rMProjectService.create(this.rMProject));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRMProject>>) {
        result.subscribe((res: HttpResponse<IRMProject>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get rMProject() {
        return this._rMProject;
    }

    set rMProject(rMProject: IRMProject) {
        this._rMProject = rMProject;
    }
}
