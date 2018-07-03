import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRMCategory } from 'app/shared/model/rm-category.model';
import { RMCategoryService } from './rm-category.service';

@Component({
    selector: '-rm-category-delete-dialog',
    templateUrl: './rm-category-delete-dialog.component.html'
})
export class RMCategoryDeleteDialogComponent {
    rMCategory: IRMCategory;

    constructor(private rMCategoryService: RMCategoryService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rMCategoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'rMCategoryListModification',
                content: 'Deleted an rMCategory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: '-rm-category-delete-popup',
    template: ''
})
export class RMCategoryDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMCategory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RMCategoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.rMCategory = rMCategory;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
