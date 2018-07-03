/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReqmanTestModule } from '../../../test.module';
import { RMTagDeleteDialogComponent } from 'app/entities/rm-tag/rm-tag-delete-dialog.component';
import { RMTagService } from 'app/entities/rm-tag/rm-tag.service';

describe('Component Tests', () => {
    describe('RMTag Management Delete Component', () => {
        let comp: RMTagDeleteDialogComponent;
        let fixture: ComponentFixture<RMTagDeleteDialogComponent>;
        let service: RMTagService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMTagDeleteDialogComponent]
            })
                .overrideTemplate(RMTagDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RMTagDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMTagService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
