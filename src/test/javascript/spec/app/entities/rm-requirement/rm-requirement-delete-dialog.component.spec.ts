/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReqmanTestModule } from '../../../test.module';
import { RMRequirementDeleteDialogComponent } from 'app/entities/rm-requirement/rm-requirement-delete-dialog.component';
import { RMRequirementService } from 'app/entities/rm-requirement/rm-requirement.service';

describe('Component Tests', () => {
    describe('RMRequirement Management Delete Component', () => {
        let comp: RMRequirementDeleteDialogComponent;
        let fixture: ComponentFixture<RMRequirementDeleteDialogComponent>;
        let service: RMRequirementService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMRequirementDeleteDialogComponent]
            })
                .overrideTemplate(RMRequirementDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RMRequirementDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMRequirementService);
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
