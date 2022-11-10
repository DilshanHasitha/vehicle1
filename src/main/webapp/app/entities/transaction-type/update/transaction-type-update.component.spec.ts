import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TransactionTypeFormService } from './transaction-type-form.service';
import { TransactionTypeService } from '../service/transaction-type.service';
import { ITransactionType } from '../transaction-type.model';

import { TransactionTypeUpdateComponent } from './transaction-type-update.component';

describe('TransactionType Management Update Component', () => {
  let comp: TransactionTypeUpdateComponent;
  let fixture: ComponentFixture<TransactionTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transactionTypeFormService: TransactionTypeFormService;
  let transactionTypeService: TransactionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TransactionTypeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TransactionTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransactionTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionTypeFormService = TestBed.inject(TransactionTypeFormService);
    transactionTypeService = TestBed.inject(TransactionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const transactionType: ITransactionType = { id: 456 };

      activatedRoute.data = of({ transactionType });
      comp.ngOnInit();

      expect(comp.transactionType).toEqual(transactionType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionType>>();
      const transactionType = { id: 123 };
      jest.spyOn(transactionTypeFormService, 'getTransactionType').mockReturnValue(transactionType);
      jest.spyOn(transactionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionType }));
      saveSubject.complete();

      // THEN
      expect(transactionTypeFormService.getTransactionType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionTypeService.update).toHaveBeenCalledWith(expect.objectContaining(transactionType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionType>>();
      const transactionType = { id: 123 };
      jest.spyOn(transactionTypeFormService, 'getTransactionType').mockReturnValue({ id: null });
      jest.spyOn(transactionTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionType }));
      saveSubject.complete();

      // THEN
      expect(transactionTypeFormService.getTransactionType).toHaveBeenCalled();
      expect(transactionTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionType>>();
      const transactionType = { id: 123 };
      jest.spyOn(transactionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
