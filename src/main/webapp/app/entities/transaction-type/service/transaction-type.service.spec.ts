import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITransactionType } from '../transaction-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../transaction-type.test-samples';

import { TransactionTypeService } from './transaction-type.service';

const requireRestSample: ITransactionType = {
  ...sampleWithRequiredData,
};

describe('TransactionType Service', () => {
  let service: TransactionTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransactionType | ITransactionType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransactionTypeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TransactionType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const transactionType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transactionType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransactionType', () => {
      const transactionType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transactionType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransactionType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransactionType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TransactionType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTransactionTypeToCollectionIfMissing', () => {
      it('should add a TransactionType to an empty array', () => {
        const transactionType: ITransactionType = sampleWithRequiredData;
        expectedResult = service.addTransactionTypeToCollectionIfMissing([], transactionType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionType);
      });

      it('should not add a TransactionType to an array that contains it', () => {
        const transactionType: ITransactionType = sampleWithRequiredData;
        const transactionTypeCollection: ITransactionType[] = [
          {
            ...transactionType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransactionTypeToCollectionIfMissing(transactionTypeCollection, transactionType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransactionType to an array that doesn't contain it", () => {
        const transactionType: ITransactionType = sampleWithRequiredData;
        const transactionTypeCollection: ITransactionType[] = [sampleWithPartialData];
        expectedResult = service.addTransactionTypeToCollectionIfMissing(transactionTypeCollection, transactionType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionType);
      });

      it('should add only unique TransactionType to an array', () => {
        const transactionTypeArray: ITransactionType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transactionTypeCollection: ITransactionType[] = [sampleWithRequiredData];
        expectedResult = service.addTransactionTypeToCollectionIfMissing(transactionTypeCollection, ...transactionTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transactionType: ITransactionType = sampleWithRequiredData;
        const transactionType2: ITransactionType = sampleWithPartialData;
        expectedResult = service.addTransactionTypeToCollectionIfMissing([], transactionType, transactionType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionType);
        expect(expectedResult).toContain(transactionType2);
      });

      it('should accept null and undefined values', () => {
        const transactionType: ITransactionType = sampleWithRequiredData;
        expectedResult = service.addTransactionTypeToCollectionIfMissing([], null, transactionType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionType);
      });

      it('should return initial array if no TransactionType is added', () => {
        const transactionTypeCollection: ITransactionType[] = [sampleWithRequiredData];
        expectedResult = service.addTransactionTypeToCollectionIfMissing(transactionTypeCollection, undefined, null);
        expect(expectedResult).toEqual(transactionTypeCollection);
      });
    });

    describe('compareTransactionType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransactionType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTransactionType(entity1, entity2);
        const compareResult2 = service.compareTransactionType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTransactionType(entity1, entity2);
        const compareResult2 = service.compareTransactionType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTransactionType(entity1, entity2);
        const compareResult2 = service.compareTransactionType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
